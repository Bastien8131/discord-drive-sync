package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.core.services.CoreMessageServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.*;
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
    private final DiscordMessageDataServices discordMessageDataServices;
    private final DiscordMessageServices discordMessageServices;

    private final CoreMessageServices coreMessageServices;

    private final S3SyncServices s3SyncServices;

    public MessageSyncServices(DiscordApiServices discordApiServices, DiscordUserServices discordUserServices, DiscordChannelServices discordChannelServices, DiscordMessageDataServices discordMessageDataServices, DiscordMessageServices discordMessageServices, CoreMessageServices coreMessageServices, S3SyncServices s3SyncServices) {
        this.discordApiServices = discordApiServices;
        this.discordUserServices = discordUserServices;
        this.discordChannelServices = discordChannelServices;
        this.discordMessageDataServices = discordMessageDataServices;
        this.discordMessageServices = discordMessageServices;
        this.coreMessageServices = coreMessageServices;
        this.s3SyncServices = s3SyncServices;
    }

    public void newMessageFormDiscord(Message message) {
        // at the end, bot replace user message, but with the same content
        discordApiServices.deleteMessage(message);

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

        discordUserServices.getOrFetchById(authorId).ifPresentOrElse(discordUser -> {
            discordChannelServices.getById(channelId).ifPresentOrElse(discordChannelSource -> {

                s3SyncServices.getFilesFromMessage(message, discordUser).thenAccept(driveFiles -> {

                    //create data obj or fetch from db if alrady exsist
                    DiscordMessageData discordMessageData;
                    if (discordMessageDataServices.dataAlreadyExists(message)){
                        discordMessageData = discordMessageDataServices.getByContent(message.getContentDisplay());
                    } else {
                        discordMessageData = new DiscordMessageData(message.getContentDisplay());
                        discordMessageDataServices.save(discordMessageData);
                    }

                    discordApiServices.sendMultipleMessages(discordChannelTargetsList, message, driveFiles).thenAccept(botMessages -> {

                        for (Message botMessage : botMessages) {
                            long botMsgChannelId = botMessage.getChannelIdLong();
                            discordChannelTargetsList.stream().filter(
                                    discordChannel -> discordChannel.getId() == botMsgChannelId).findFirst().ifPresentOrElse(
                                    discordChannel -> {
                                        CoreMessage coreMessage = new CoreMessage(
                                                botMessage.getIdLong(),
                                                discordMessageData,
                                                discordUser,
                                                discordChannel
                                        );

                                        coreMessage.setDriveFiles(driveFiles);
                                        coreMessage.addLabel(discordChannelSource.getLabel());
                                        coreMessageServices.save(coreMessage);
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
        discordMessageServices.getById(messageId).ifPresentOrElse(
                discordMessageServices::delete,
                () -> LogMessages.notFoundInDatabase(log, DiscordMessage.class, messageId)
        );
    }
}
