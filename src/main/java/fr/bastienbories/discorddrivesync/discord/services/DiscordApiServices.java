package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class DiscordApiServices {

    private final JDA jda;
    private final Guild guild;

    private final Set<Long> botDeletedCategoryIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> botDeletedChannelIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> botDeletedMessageIds = ConcurrentHashMap.newKeySet();

    public DiscordApiServices(JDA jda) {
        this.jda = jda;
        this.guild = jda.getGuilds().getFirst();
    }

    public List<Member> getMembers() {
        List<Member> members = guild.loadMembers().get();
        System.out.println(members);
        return members;
    }

    public Category createCategory(String name) {
//        Guild guild = jda.getGuilds().getFirst();
        return guild.createCategory(name).complete();
    }

//    public Message sendMessage(long idCategory, long idChannel, Message message){
//        MessageCreateData messageCreateData = new MessageCreateData();
//        return guild.getTextChannelById(idChannel).sendMessage(messageCreateData).complete();
//    }

    public List<Message> sendMultipleMessages(List<DiscordChannel> channels, Message message) {
        List<Message> messages = new ArrayList<>();

        for (DiscordChannel discordChannel : channels) {
            TextChannel textChannel = jda.getTextChannelById(discordChannel.getId());
            if (textChannel != null) {
                messages.add(textChannel.sendMessage(message.getContentRaw().replaceAll("<#\\d+>\\s*", "").trim()).complete());
            }
        }

        return messages;
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
        Objects.requireNonNull(guild.getTextChannelById(message.getChannelId())).deleteMessageById(message.getId()).complete();
    }
}
