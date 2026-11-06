package fr.bastienbories.discorddrivesync.discord.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.drive.controller.DriveFileController;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.result.DriveFileResult.Failure;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;

@Service
@Transactional
public class DiscordApiServices {

    private static final Logger log = LoggerFactory.getLogger(DiscordApiServices.class);

    private final JDA jda;
    private final Guild guild;

    private final Set<Long> botDeletedCategoryIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> botDeletedChannelIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> botDeletedMessageIds = ConcurrentHashMap.newKeySet();

    private final DriveFileServices driveFileServices;

    public DiscordApiServices(JDA jda, DriveFileServices driveFileServices) throws InterruptedException {
        this.jda = jda;
        List<Guild> guilds = jda.awaitReady().getGuilds();
        this.guild = guilds.stream().findFirst().orElseThrow(
                () -> new IllegalStateException("This bot is not associated with any server")
        );
        this.driveFileServices = driveFileServices;
    }

//    private String removeChannelTag(String contentRaw){
//        return contentRaw.replaceAll("<#\\d+>\\s*", "").trim();
//    }

    private String buildMessageContent(String content, List<DriveFile> driveFiles, List<CoreLink> links){
        StringBuilder contentBuild = new StringBuilder();
//        String messageContent = TextUtils.removeChannelTagFromContent(content);

        contentBuild.append(content);

        if (!driveFiles.isEmpty()){ contentBuild.append("\n"); }
        for (DriveFile driveFile: driveFiles){
            contentBuild.append("\n");
            contentBuild.append(driveFileServices.buildDownloadLink(driveFile));
        }

        if (!links.isEmpty()){ contentBuild.append("\n"); }
        for (CoreLink coreLink: links){
            contentBuild.append("\n");
            contentBuild.append(coreLink.getUrl());
        }

        return contentBuild.toString();
    }

    public List<Member> getMembers() {
        List<Member> members = guild.loadMembers().get();
        return members;
    }

//    public Message sendMessage(long idCategory, long idChannel, Message message){
//        MessageCreateData messageCreateData = new MessageCreateData();
//        return guild.getTextChannelById(idChannel).sendMessage(messageCreateData).complete();
//    }

    public CompletableFuture<List<Message>> sendMultipleMessages(List<DiscordChannel> channels, String content, List<DriveFile> driveFiles, List<CoreLink> links) {
        List<CompletableFuture<Message>> messages = new ArrayList<>();

        for (DiscordChannel discordChannel : channels) {
            TextChannel textChannel = jda.getTextChannelById(discordChannel.getId());
            if (textChannel != null) {
                messages.add(textChannel.sendMessage(buildMessageContent(content, driveFiles, links)).submit());
            }
        }

        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(messages.toArray(new CompletableFuture[messages.size()]));
        return allFuturesResult.thenApply(v ->
                messages.stream()
                        .map(future -> future.join())
                        .collect(Collectors.toList())
        );
    }

    public CompletableFuture<List<Message>> sendMultipleComment(List<DiscordMessage> discordMessageTargetsList, Message comment) {
        List<CompletableFuture<Message>> messages = new ArrayList<>();

        for (DiscordMessage discordMessage: discordMessageTargetsList){
            TextChannel textChannel = jda.getTextChannelById(discordMessage.getDiscordChannel().getId());
            if (textChannel != null){
                CompletableFuture<Message> message = textChannel.retrieveMessageById(discordMessage.getId()).submit().thenCompose(
                        msg -> msg.reply(TextUtils.removeChannelTagFromContent(comment.getContentRaw())).submit()
                );
                messages.add(message);
            }
        }

        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(messages.toArray(new CompletableFuture[messages.size()]));
        return allFuturesResult.thenApply(v ->
                messages.stream()
                        .map(future -> future.join())
                        .collect(Collectors.<Message>toList())
        );
    }


    public long getBotId() {
        return jda.getSelfUser().getIdLong();
    }

    public boolean thisMessageIsDeleteByBot(long messageId) {
        return botDeletedMessageIds.remove(messageId);
    }

    public boolean thisChannelIsDeleteByBot(long channelId) {
        return  botDeletedChannelIds.remove(channelId);
    }

    public boolean thisCategoryIsDeleteByBot(long categoryId) {
        return  botDeletedCategoryIds.remove(categoryId);
    }

    public void deleteMessage(Message message) {
        botDeletedMessageIds.add(message.getIdLong());
        Objects.requireNonNull(guild.getTextChannelById(message.getChannelId())).deleteMessageById(message.getId()).queue();
    }

    public void deleteChannel(TextChannel channel){
        botDeletedChannelIds.add(channel.getIdLong());
        Objects.requireNonNull(guild.getTextChannelById(channel.getIdLong())).delete().queue();
    }

    public InteractionHook getHook(String interactionToken) {
        return InteractionHook.from(jda, interactionToken);
    }

    public void reportDriveFilesFailures(List<Failure> failures, CoreUploadLink link) {
        StringBuilder contentBuild = new StringBuilder();

        contentBuild.append("The upload of these files could not be completed : \n");

        for (Failure failure : failures) {
            contentBuild.append("\n");
            contentBuild.append(String.format("- %s", failure.filename()));
        }

        if (!link.isInteractionExpired()) {
            InteractionHook hook = getHook(link.getInteractionToken());
            hook.sendMessage(contentBuild.toString()).setEphemeral(true).queue();
        } else{
            // Send message in DM
            long userId = link.getUser().getId();
            jda.retrieveUserById(userId)
                .flatMap(user -> user.openPrivateChannel())
                .flatMap(channel -> channel.sendMessage(contentBuild.toString()))
                .queue(null, failure -> {
                    LogMessages.unexpectedError(log, failure);
                });
        }
    }
}
