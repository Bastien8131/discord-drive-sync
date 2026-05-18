package fr.bastienbories.discorddrivesync.discord.services;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscordApiServices {

    private final JDA jda;

    public DiscordApiServices(JDA jda) {
        this.jda = jda;
    }

    public List<Member> getMembers() {
        Guild guild = jda.getGuilds().getFirst();
        return guild.loadMembers().get();
    }

    public Category createCategory(String name) {
        Guild guild = jda.getGuilds().getFirst();
        return guild.createCategory(name).complete();
    }
}
