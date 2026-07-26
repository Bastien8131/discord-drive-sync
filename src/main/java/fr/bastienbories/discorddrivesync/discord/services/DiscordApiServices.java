package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileUrlServices;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiscordApiServices {

    private final JDA jda;
    private final Guild guild;

    private final Set<Long> botDeletedCategoryIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> botDeletedChannelIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> botDeletedMessageIds = ConcurrentHashMap.newKeySet();

    private final DriveFileUrlServices driveFileUrlServices;

    public DiscordApiServices(JDA jda, DriveFileUrlServices driveFileUrlServices) throws InterruptedException {
        this.jda = jda;
        List<Guild> guilds = jda.awaitReady().getGuilds();
        this.guild = guilds.stream().findFirst().orElseThrow(
                () -> new IllegalStateException("This bot is not associated with any server")
        );
        this.driveFileUrlServices = driveFileUrlServices;
    }

    private String removeChannelTag(String contentRaw){
        return contentRaw.replaceAll("<#\\d+>\\s*", "").trim();
    }

    private String buildMessageContent(Message message, List<DriveFile> driveFiles){
        StringBuilder contentBuild = new StringBuilder();
        String messageContent = removeChannelTag(message.getContentRaw());

        contentBuild.append(messageContent);
        contentBuild.append("\n");

        for (DriveFile driveFile: driveFiles){
            contentBuild.append("\n");
            contentBuild.append(driveFileUrlServices.buildUrl(driveFile));
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

    public CompletableFuture<List<Message>> sendMultipleMessages(List<DiscordChannel> channels, Message message, List<DriveFile> driveFiles) {
        List<CompletableFuture<Message>> messages = new ArrayList<>();

        for (DiscordChannel discordChannel : channels) {
            TextChannel textChannel = jda.getTextChannelById(discordChannel.getId());
            if (textChannel != null) {
                messages.add(textChannel.sendMessage(buildMessageContent(message, driveFiles)).submit());
            }
        }

        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(messages.toArray(new CompletableFuture[messages.size()]));
        return allFuturesResult.thenApply(v ->
                messages.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.<Message>toList())
        );
    }

    public CompletableFuture<List<Message>> sendMultipleComment(List<DiscordMessage> discordMessageTargetsList, Message comment) {
        List<CompletableFuture<Message>> messages = new ArrayList<>();

        for (DiscordMessage discordMessage: discordMessageTargetsList){
            TextChannel textChannel = jda.getTextChannelById(discordMessage.getDiscordChannel().getId());
            if (textChannel != null){
                CompletableFuture<Message> message = textChannel.retrieveMessageById(discordMessage.getId()).submit().thenCompose(
                        msg -> msg.reply(removeChannelTag(comment.getContentRaw())).submit()
                );
                messages.add(message);
            }
        }

        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(messages.toArray(new CompletableFuture[messages.size()]));
        return allFuturesResult.thenApply(v ->
                messages.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.<Message>toList())
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
}
