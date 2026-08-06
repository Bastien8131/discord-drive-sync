package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.services.CoreContentServices;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import fr.bastienbories.discorddrivesync.core.services.CoreLinkServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.*;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageSyncServices {

    private static final Logger log = LoggerFactory.getLogger(MessageSyncServices.class);

    private final DiscordApiServices discordApiServices;

    private final DiscordUserServices discordUserServices;
    private final DiscordChannelServices discordChannelServices;
    private final CoreContentServices coreContentServices;
    private final DiscordMessageServices discordMessageServices;

    private final CoreLinkServices coreLinkServices;
    private final CoreLabelServices coreLabelServices;

    private final DriveFileServices driveFileServices;
    private final S3SyncServices s3SyncServices;

    public MessageSyncServices(DiscordApiServices discordApiServices, DiscordUserServices discordUserServices, DiscordChannelServices discordChannelServices, CoreContentServices coreContentServices, DiscordMessageServices discordMessageServices, CoreLinkServices coreLinkServices, CoreLabelServices coreLabelServices, DriveFileServices driveFileServices, S3SyncServices s3SyncServices) {
        this.discordApiServices = discordApiServices;
        this.discordUserServices = discordUserServices;
        this.discordChannelServices = discordChannelServices;
        this.coreContentServices = coreContentServices;
        this.discordMessageServices = discordMessageServices;
        this.coreLinkServices = coreLinkServices;
        this.coreLabelServices = coreLabelServices;
        this.driveFileServices = driveFileServices;
        this.s3SyncServices = s3SyncServices;
    }

    public void newMessageFormDiscord(Message message) {
        //check message is valid
        if (discordMessageServices.checkIsNotValid(message)) { return; }

        long authorId = message.getAuthor().getIdLong();
        long channelId = message.getChannelIdLong();

        //get the list of channel mentioned in the message
        //if not mention, bot take the channel source of message send
        List<DiscordChannel> discordChannelTargetsList = new ArrayList<>();
        List<GuildChannel> mentionedChannels = message.getMentions().getChannels();
        if (mentionedChannels.isEmpty()) {
            long id = message.getChannelIdLong();
            discordChannelServices.getById(id).ifPresentOrElse(
                    discordChannelTargetsList::add,
                    () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, id)
            );
        } else {
            for (GuildChannel channel : mentionedChannels) {
                long id = channel.getIdLong();
                discordChannelServices.getById(id).ifPresentOrElse(
                        discordChannelTargetsList::add,
                        () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, id)
                );
            }
        }

        //resolve label ids now, while the session is still open (the DiscordChannel proxy will be detached once the async callbacks run)
        List<Long> discordChannelTargetLabelIds = discordChannelTargetsList.stream()
                .map(discordChannel -> discordChannel.getLabel().getId())
                .toList();

        discordUserServices.getOrFetchById(authorId).ifPresentOrElse(discordUser -> {
            discordChannelServices.getById(channelId).ifPresentOrElse(discordChannelSource -> {

                s3SyncServices.getFilesFromMessageAndUpload(message, discordUser).thenAccept(driveFiles -> {

                    discordApiServices.deleteMessage(message);

                    //create data obj or fetch from db if alrady exsist
                    CoreContent coreContent = coreContentServices.getOrCreateAndAddDriveFiles(discordUser, message.getContentRaw(), driveFiles);

                    //tag the content with the label of every channel it is broadcast to
                    for (Long labelId : discordChannelTargetLabelIds) {
                        coreLabelServices.addCoreContent(labelId, coreContent);
                    }

                    List<CoreLink> links = coreLinkServices.getOrCreateLinks(message.getContentDisplay(), coreContent);

                    discordApiServices.sendMultipleMessages(discordChannelTargetsList, coreContent.getText(), driveFiles, links).thenAccept(botMessages -> {

                        for (Message botMessage : botMessages) {
                            long botMsgChannelId = botMessage.getChannelIdLong();
                            discordChannelTargetsList.stream().filter(
                                    discordChannel -> discordChannel.getId() == botMsgChannelId).findFirst().ifPresentOrElse(
                                    discordChannel -> {
                                        DiscordMessage discordMessage = new DiscordMessage(
                                                botMessage.getIdLong(),
                                                coreContent,
                                                discordUser,
                                                discordChannel
                                        );

                                        discordMessageServices.save(discordMessage);
                                    },
                                    () -> LogMessages.notFoundInTheList(log, DiscordChannel.class, botMsgChannelId, discordChannelTargetsList)
                            );
                        }
                    }).exceptionally(
                            ex -> {
                                LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
                                return null;
                            }
                    );
                }).exceptionally(ex -> {
                    LogMessages.unexpectedError(log, ex);
                    return null;
                });
            }, () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, channelId));
        }, () -> LogMessages.notFoundInDatabase(log, DiscordUser.class, authorId));
    }

    public void deleteMessageFromDiscord(@NonNull MessageDeleteEvent event) {
        // returns whether the bot deleted the message
        // function get only event from users<
        if (discordApiServices.thisMessageIsDeleteByBot(event.getMessageIdLong())) return;

        long messageId = event.getMessageIdLong();
        discordMessageServices.getById(messageId).ifPresentOrElse(discordMessage -> {
            CoreContent coreContent = discordMessage.getContent();
            discordMessageServices.delete(discordMessage);

            if (!discordMessageServices.dataExistsInSomeChannel(coreContent)) {
                driveFileServices.findByCoreContent(coreContent).ifPresentOrElse(
                        s3SyncServices::deleteMultipleFiles,
                        () -> LogMessages.listNotFoundInDatabase(log, DriveFile.class, CoreContent.class, coreContent.getId())
                );
            }
        }, () -> LogMessages.notFoundInDatabase(log, DiscordMessage.class, messageId));
    }
}
