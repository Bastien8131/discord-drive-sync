package fr.bastienbories.discorddrivesync.sync;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.services.CoreContentServices;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import fr.bastienbories.discorddrivesync.core.services.CoreLinkServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.DiscordApiServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordMessageServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

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
        List<DiscordChannel> targetChannels = new ArrayList<>();
        List<GuildChannel> mentionedChannels = message.getMentions().getChannels();
        if (mentionedChannels.isEmpty()) {
            long id = message.getChannelIdLong();
            discordChannelServices.getById(id).ifPresentOrElse(
                    targetChannels::add,
                    () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, id)
            );
        } else {
            for (GuildChannel channel : mentionedChannels) {
                long id = channel.getIdLong();
                discordChannelServices.getById(id).ifPresentOrElse(
                        targetChannels::add,
                        () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, id)
                );
            }
        }

        discordUserServices.getOrFetchById(authorId).ifPresentOrElse(discordUser -> {
            discordChannelServices.getById(channelId).ifPresentOrElse(discordChannelSource -> {
                s3SyncServices.uploadAttachments(message.getAttachments(), discordUser).thenAccept(driveFiles -> {

                    discordApiServices.deleteMessage(message);
                    createDiscordMessage(discordUser, message.getContentRaw(), targetChannels, driveFiles);

                }).exceptionally(ex -> {
                    LogMessages.unexpectedError(log, ex);
                    return null;
                });
            }, () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, channelId));
        }, () -> LogMessages.notFoundInDatabase(log, DiscordUser.class, authorId));
    }

    public void createDiscordMessage(
            DiscordUser author, String text,
            List<DiscordChannel> targetChannels,
            List<DriveFile> driveFiles
        ){
            
        CoreContent coreContent = coreContentServices.getOrCreateAndAddDriveFiles(author, text, driveFiles);
        List<Long> labelIds = coreLabelServices.getLabelIdsByDiscordChannelList(targetChannels);

        for (Long labelId : labelIds) {
            coreLabelServices.addCoreContent(labelId, coreContent);
        }

        List<CoreLink> links = coreLinkServices.getOrCreateLinks(text, coreContent);

        discordApiServices.sendMultipleMessages(targetChannels, coreContent.getText(), driveFiles, links).thenAccept(botMessages -> {

            for (Message botMessage : botMessages) {
                long botMsgChannelId = botMessage.getChannelIdLong();
                targetChannels.stream().filter(
                        discordChannel -> discordChannel.getId() == botMsgChannelId).findFirst().ifPresentOrElse(
                        discordChannel -> {
                            DiscordMessage discordMessage = new DiscordMessage(
                                    botMessage.getIdLong(),
                                    coreContent,
                                    author,
                                    discordChannel
                            );

                            discordMessageServices.save(discordMessage);
                        },
                        () -> LogMessages.notFoundInTheList(log, DiscordChannel.class, botMsgChannelId, targetChannels)
                );
            }
        }).exceptionally(
                ex -> {
                    LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
                    return null;
                }
        );
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
                        s3SyncServices::deleteAll,
                        () -> LogMessages.listNotFoundInDatabase(log, DriveFile.class, CoreContent.class, coreContent.getId())
                );
            }
        }, () -> LogMessages.notFoundInDatabase(log, DiscordMessage.class, messageId));
    }
}
