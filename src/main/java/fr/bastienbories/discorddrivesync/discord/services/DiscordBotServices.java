package fr.bastienbories.discorddrivesync.discord.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.sync.CategorySyncServices;
import fr.bastienbories.discorddrivesync.sync.ChannelSyncServices;
import fr.bastienbories.discorddrivesync.sync.CommentSyncServices;
import fr.bastienbories.discorddrivesync.sync.MessageSyncServices;
import fr.bastienbories.discorddrivesync.sync.UserSyncServices;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Service
public class DiscordBotServices extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(DiscordBotServices.class);

    private final CategorySyncServices categorySyncServices;
    private final ChannelSyncServices channelSyncServices;
    private final CommentSyncServices commentSyncServices;
    private final MessageSyncServices messageSyncServices;
    private final UserSyncServices userSyncServices;

    private final JDA jda;
    private final Executor discordTaskExecutor;

    public DiscordBotServices(CategorySyncServices categorySyncServices, ChannelSyncServices channelSyncServices, CommentSyncServices commentSyncServices, MessageSyncServices messageSyncServices, UserSyncServices userSyncServices, JDA jda, Executor discordTaskExecutor) {
        this.categorySyncServices = categorySyncServices;
        this.channelSyncServices = channelSyncServices;
        this.commentSyncServices = commentSyncServices;
        this.messageSyncServices = messageSyncServices;
        this.userSyncServices = userSyncServices;
        this.jda = jda;
        this.discordTaskExecutor = discordTaskExecutor;
    }

    @PostConstruct
    public void init() {
        jda.addEventListener(this);
        CompletableFuture.runAsync(userSyncServices::updateUserTable, discordTaskExecutor);
    }

    //Listener

    @Override
    public void onGenericEvent(@NonNull GenericEvent event) {
        super.onGenericEvent(event);
    }

    @Override
    public void onGuildMemberJoin(@NonNull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) return;
        CompletableFuture.runAsync(userSyncServices::updateUserTable, discordTaskExecutor);
    }

    @Override
    public void onGuildMemberRemove(@NonNull GuildMemberRemoveEvent event) {
        if (event.getUser().isBot()) return;
        CompletableFuture.runAsync(userSyncServices::updateUserTable, discordTaskExecutor);
    }

    @Override
    public void onChannelCreate(@NonNull ChannelCreateEvent event) {
        super.onChannelCreate(event);
        switch (event.getChannelType()){
            case ChannelType.CATEGORY -> categorySyncServices.createCategoryFromDiscord(event.getChannel().asCategory());
            case ChannelType.TEXT -> channelSyncServices.createChannelFromDiscord(event.getChannel().asTextChannel());
            case ChannelType.VOICE -> CompletableFuture.runAsync(userSyncServices::updateUserTable, discordTaskExecutor);
            default -> LogMessages.enumTypeIsNotExpected(log, ChannelType.class, event.getChannelType(), "onChannelCreate");
        }

    }

    @Override
    public void onChannelDelete(@NonNull ChannelDeleteEvent event) {
        super.onChannelDelete(event);
        switch (event.getChannelType()){
            case ChannelType.CATEGORY -> categorySyncServices.deleteCategoryFromDiscord(event.getChannel().asCategory());
            case ChannelType.TEXT -> channelSyncServices.deleteChannelFromDiscord(event.getChannel().asTextChannel());
            default -> LogMessages.enumTypeIsNotExpected(log, ChannelType.class, event.getChannelType(), "onChannelDelete");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        super.onMessageReceived(event);
//        MessageType.INLINE_REPLY
//        event.getMessage().getMentions().getChannels();
        if (event.getMessage().getType() == MessageType.INLINE_REPLY){
            commentSyncServices.commentMessageFormDiscord(event.getMessage());
        }else{
            messageSyncServices.newMessageFormDiscord(event.getMessage());
        }
    }

    @Override
    public void onMessageDelete(@NonNull MessageDeleteEvent event) {
        super.onMessageDelete(event);
        messageSyncServices.deleteMessageFromDiscord(event);
    }
}
