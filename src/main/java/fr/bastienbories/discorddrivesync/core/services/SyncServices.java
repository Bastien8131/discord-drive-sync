package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.discord.model.*;
import fr.bastienbories.discorddrivesync.discord.services.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class SyncServices {

    private static final Logger log = LoggerFactory.getLogger(SyncServices.class);

    private final DiscordApiServices discordApiServices;

    private final DiscordUserServices discordUserServices;
    private final DiscordCategoryServices discordCategoryServices;
    private final DiscordChannelServices discordChannelServices;
    private final DiscordMessageDataServices discordMessageDataServices;
    private final DiscordMessageServices discordMessageServices;
    private final DiscordCommentServices discordCommentServices;

    private final CoreCategoryServices coreCategoryServices;
    private final CoreMessageServices coreMessageServices;
    private final CoreLabelServices coreLabelServices;

    public SyncServices(DiscordApiServices discordApiServices, DiscordUserServices discordUserServices, DiscordCategoryServices discordCategoryServices, DiscordChannelServices discordChannelServices, DiscordMessageDataServices discordMessageDataServices, DiscordMessageServices discordMessageServices, DiscordCommentServices discordCommentServices, CoreCategoryServices coreCategoryServices, CoreMessageServices coreMessageServices, CoreLabelServices coreLabelServices) {
        this.discordApiServices = discordApiServices;
        this.discordUserServices = discordUserServices;
        this.discordCategoryServices = discordCategoryServices;
        this.discordChannelServices = discordChannelServices;
        this.discordMessageDataServices = discordMessageDataServices;
        this.discordMessageServices = discordMessageServices;
        this.discordCommentServices = discordCommentServices;
        this.coreCategoryServices = coreCategoryServices;
        this.coreMessageServices = coreMessageServices;
        this.coreLabelServices = coreLabelServices;
    }

    //---Category

    public void createCategoryFromDiscord(Category channel){
        DiscordCategory discordCategory = new DiscordCategory(channel.getIdLong(), channel.getName());
        CoreCategory coreCategory = new CoreCategory(channel.getName(), discordCategory);
        discordCategoryServices.save(discordCategory);
        coreCategoryServices.save(coreCategory);
    }

    public void deleteCategoryFromDiscord(Category channel){
        long id = channel.getIdLong();
        Optional<CoreCategory> coreCategory = coreCategoryServices.getByDiscordId(id);
        if (coreCategory.isEmpty()) {
            LogMessages.notFoundInDatabase(log, CoreCategory.class, id);
            return;
        }
        coreCategoryServices.delete(coreCategory.get());
        discordCategoryServices.deleteById(channel.getIdLong());
    }

    //---Channel

    public void createChannelFromDiscord(TextChannel channel){
        long idCategory = channel.getParentCategoryIdLong();
        CoreLabel label = coreLabelServices.getOrCreateLabelByName(channel.getName().toLowerCase());
        Optional<DiscordCategory> discordCategory = discordCategoryServices.getById(idCategory);

        if (discordCategory.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordCategory.class, idCategory);
            return;
        }

        DiscordChannel discordChannel = new DiscordChannel(
                channel.getIdLong(),
                channel.getName(),
                channel.getType(),
                label,
                discordCategory.get()
        );
        discordChannelServices.save(discordChannel);
    }

    public void deleteChannelFromDiscord(TextChannel channel){
        long id = channel.getIdLong();
        Optional<DiscordChannel> discordChannel = discordChannelServices.getById(id);

        if (discordChannel.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordChannel.class, id);
            return;
        }

        discordChannelServices.delete(discordChannel.get());
    }

    //---Message

    public void newMessageFormDiscord(Message message) {
        // at the end, bot replace user message, but with the same content
        discordApiServices.deleteMessage(message);

        //check message is valid
        if (discordMessageServices.checkIsNotValid(message)) {
            return;
        }

        long authorId = message.getAuthor().getIdLong();
        Optional<DiscordUser> discordUser = discordUserServices.getOrFetchById(authorId);
        if (discordUser.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordUser.class, authorId);
            return;
        }

        long channelId = message.getChannelIdLong();
        Optional<DiscordChannel> discordChannelSource = discordChannelServices.getById(channelId);
        if (discordChannelSource.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordChannel.class, channelId);
            return;
        }

        CoreLabel coreLabel = discordChannelSource.get().getLabel();

        //create data obj or fetch from db if alrady exsist
        DiscordMessageData discordMessageData;
        if (discordMessageDataServices.dataAlreadyExists(message)){
            discordMessageData = discordMessageDataServices.getByContent(message.getContentDisplay());
        } else {
            discordMessageData = new DiscordMessageData(message.getContentDisplay());
            discordMessageDataServices.save(discordMessageData);
        }

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

        //bot send message in all mentioned channels and return them
        discordApiServices.sendMultipleMessages(discordChannelTargetsList, message).thenAccept(botMessages -> {

            for (Message botMessage : botMessages) {
                long botMsgChannelId = botMessage.getChannelIdLong();
                discordChannelTargetsList.stream().filter(
                        discordChannel -> discordChannel.getId() == botMsgChannelId).findFirst().ifPresentOrElse(
                        discordChannel -> {
                            CoreMessage coreMessage = new CoreMessage(
                                    botMessage.getIdLong(),
                                    discordMessageData,
                                    discordUser.get(),
                                    discordChannel
                            );

                            coreMessage.addLabel(coreLabel);
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
    }

    public void deleteMessageFromDiscord(@NonNull MessageDeleteEvent event) {
        // returns whether the bot deleted the message
        // function get only event from users<
        if (discordApiServices.thisMessageIsDeleteByBot(event.getMessageIdLong())) return;

        long messageId = event.getMessageIdLong();
        Optional<DiscordMessage> discordMessage = discordMessageServices.getById(messageId);
        if (discordMessage.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordMessage.class, messageId);
            return;
        }
        discordMessageServices.delete(discordMessage.get());
    }

    //---Comment

    public void commentMessageFormDiscord(Message comment) {
        discordApiServices.deleteMessage(comment);
        if (comment.getReferencedMessage() == null) return;

        long authorId = comment.getAuthor().getIdLong();
        long refMessageId = comment.getReferencedMessage().getIdLong();

        Optional<DiscordUser> discordUser = discordUserServices.getOrFetchById(authorId);
        if (discordUser.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordUser.class, authorId);
            return;
        }

        DiscordMessageData discordCommentData = new DiscordMessageData(comment.getContentRaw());
        discordMessageDataServices.save(discordCommentData);

        Optional<DiscordMessage> discordRefMessageOpt = discordMessageServices.getById(refMessageId);
        if (discordRefMessageOpt.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordMessage.class, refMessageId);
            return;
        }
        DiscordMessage discordRefMessage = discordRefMessageOpt.get();
        DiscordMessageData discordRefMessageData = discordRefMessage.getDiscordMessageData();
        List<DiscordMessage> discordMessageTargetsList = discordMessageServices.getListByData(discordRefMessageData);

        discordApiServices.sendMultipleComment(discordMessageTargetsList, comment).thenAccept(botComments -> {
            for (Message botComment: botComments){

                long botChannelId = botComment.getChannelIdLong();
                Message botRefMessage = Objects.requireNonNull(botComment.getReferencedMessage());
                long botRefMessageId = botRefMessage.getIdLong();

                discordChannelServices.getById(botChannelId).ifPresentOrElse(
                        discordChannel -> {
                            discordMessageTargetsList.stream().filter(discordMessage -> discordMessage.getId() == botRefMessageId).findFirst().ifPresentOrElse(
                                    discordMessage -> {
                                        DiscordComment discordComment = new DiscordComment(
                                                botComment.getIdLong(),
                                                discordCommentData,
                                                discordUser.get(),
                                                discordChannel,
                                                discordMessage,
                                                null
                                        );

                                        discordCommentServices.save(discordComment);
                                    },
                                    () -> LogMessages.notFoundInTheList(log, DiscordMessage.class, botRefMessageId, discordMessageTargetsList)
                            );
                        },
                        () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, botChannelId)
                );
            }
        }).exceptionally(
            ex -> {
                LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
                return null;
            }
        );
    }








    public void updateUserTable() {
        discordUserServices.updateTable();
    }
}
