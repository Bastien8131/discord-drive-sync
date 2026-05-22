package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiscordApiServices {

    private final JDA jda;
    private final Guild guild;

    public DiscordApiServices(JDA jda) {
        this.jda = jda;
        this.guild = jda.getGuilds().getFirst();
    }

    public List<Member> getMembers() {
//        Guild guild = jda.getGuilds().getFirst();
        return guild.getMembers();
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
                messages.add(textChannel.sendMessage(message.getContentDisplay()).complete());
            }
        }

        return messages;
    }


    public long getBotId() {
        return jda.getSelfUser().getIdLong();
    }

    public void deleteMessage(Message message) {
        guild.getTextChannelById(message.getChannelId()).deleteMessageById(message.getId()).complete();
    }
}
