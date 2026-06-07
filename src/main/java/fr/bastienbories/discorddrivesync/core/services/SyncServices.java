package fr.bastienbories.discorddrivesync.core.services;

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
        CoreCategory coreCategory = coreCategoryServices.getByDiscordId(channel.getIdLong());
        if (coreCategory == null) return;
        coreCategoryServices.delete(coreCategory);
        discordCategoryServices.deleteById(channel.getIdLong());
    }

    //---Channel

    public void createChannelFromDiscord(TextChannel channel){
        CoreLabel label = coreLabelServices.getOrCreateLabelByName(channel.getName().toLowerCase());
        DiscordCategory discordCategory = discordCategoryServices.getById(channel.getParentCategoryIdLong());

        DiscordChannel discordChannel = new DiscordChannel(
                channel.getIdLong(),
                channel.getName(),
                channel.getType(),
                label,
                discordCategory
        );
        discordChannelServices.save(discordChannel);
    }

    public void deleteChannelFromDiscord(TextChannel channel){
        DiscordChannel discordChannel = discordChannelServices.getById(channel.getIdLong());
        if (discordChannel == null) return;
        discordChannelServices.delete(discordChannel);
    }

    //---Message

    public void newMessageFormDiscord(Message message) {
        // at the end, bot replace user message, but with the same content
        discordApiServices.deleteMessage(message);

        //check message is valid
        if (discordMessageServices.checkIsNotValid(message)) {
            return;
        }

        DiscordUser discordUser = discordUserServices.getByAuthor(message.getAuthor());
        DiscordChannel discordChannelSource = discordChannelServices.getById(message.getChannelIdLong());
        CoreLabel coreLabel = discordChannelSource.getLabel();

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
            discordChannelTargetsList.add(discordChannelServices.getById(message.getChannelIdLong()));
        } else {
            for (GuildChannel channel : mentionedChannels) {
                discordChannelTargetsList.add(discordChannelServices.getById(channel.getIdLong()));
            }
        }

        //bot send message in all mentioned channels and return them
        discordApiServices.sendMultipleMessages(discordChannelTargetsList, message).thenAccept(botMessages -> {

            //save in the bd message send by bot, but this messages are assign to the user wha ase send the source message
            //in finally, in the bd, are save only bot message
            for (Message botMessage: botMessages){
                DiscordChannel discordChannelTarget = discordChannelTargetsList.stream().filter(
                        a -> a.getId() == botMessage.getChannelIdLong()).findFirst().orElseThrow(
                        () -> new RuntimeException("Channel not found : " + botMessage.getChannelIdLong()));
                CoreMessage coreMessage = new CoreMessage(
                        botMessage.getIdLong(),
                        discordMessageData,
                        discordUser,
                        discordChannelTarget
                );

                coreMessage.addLabel(coreLabel);
                coreMessageServices.save(coreMessage);
            }

        });
    }

    public void deleteMessageFromDiscord(@NonNull MessageDeleteEvent event) {
        // returns whether the bot deleted the message
        // function get only event from users<
        if (discordApiServices.thisMessageIsDeleteByBot(event.getMessageIdLong())) return;

        DiscordMessage discordMessage = discordMessageServices.getById(event.getMessageIdLong());
        discordMessageServices.delete(discordMessage);
    }

    //---Comment

    public void commentMessageFormDiscord(Message comment) {
        discordApiServices.deleteMessage(comment);
        if (comment.getReferencedMessage() == null) return;

        DiscordUser discordUser = discordUserServices.getByAuthor(comment.getAuthor());

        DiscordMessageData discordCommentData = new DiscordMessageData(comment.getContentRaw());
        discordMessageDataServices.save(discordCommentData);

        Optional<DiscordMessage> discordRefMessageOpt = discordMessageServices.findById(comment.getReferencedMessage().getIdLong());
        if (discordRefMessageOpt.isEmpty()) {
            comment.reply("Ce message n'est pas suivi par le bot.").queue();
            return;
        }
        DiscordMessage discordRefMessage = discordRefMessageOpt.get();
        DiscordMessageData discordRefMessageData = discordRefMessage.getDiscordMessageData();
        List<DiscordMessage> discordMessageTargetsList = discordMessageServices.getListByData(discordRefMessageData);

        discordApiServices.sendMultipleComment(discordMessageTargetsList, comment).thenAccept(botComments -> {
            for (Message botComment: botComments){
                DiscordChannel discordChannel = discordChannelServices.getById(botComment.getChannelIdLong());
                DiscordMessage discordRefMessageByBot = discordMessageTargetsList.stream().filter(
                                discordMessage -> discordMessage.getId() == Objects.requireNonNull(botComment.getReferencedMessage()).getIdLong())
                        .findFirst().orElseThrow(() -> new RuntimeException("RefMessage not found"));

                DiscordComment discordComment = new DiscordComment(
                        botComment.getIdLong(),
                        discordCommentData,
                        discordUser,
                        discordChannel,
                        discordRefMessageByBot,
                        null
                );

                discordCommentServices.save(discordComment);
            }
        });
    }








    public void updateUserTable() {
        discordUserServices.updateTable();
    }

    public void createCategory(String name){
        DiscordCategory discordCategory = discordCategoryServices.getByName(name);
        CoreCategory coreCategory = new CoreCategory(name, discordCategory);
        coreCategoryServices.save(coreCategory);
    }
}
