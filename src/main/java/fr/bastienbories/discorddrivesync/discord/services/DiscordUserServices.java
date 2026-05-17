package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordUserRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscordUserServices {

    private final DiscordUserRepository discordUserRepository;

    public DiscordUserServices(DiscordUserRepository discordUserRepository) {
        this.discordUserRepository = discordUserRepository;
    }

    public DiscordUser addUser(long id, String name) {
        DiscordUser user = new DiscordUser(id, name);
        return discordUserRepository.save(user);
    }

    public void addUsers(List<Member> members){
        List<DiscordUser> users = new ArrayList<>();
        for (Member member: members){
            users.add(new DiscordUser(member.getIdLong(), member.getUser().getEffectiveName()));
        }
        discordUserRepository.saveAll(users);
    }
}
