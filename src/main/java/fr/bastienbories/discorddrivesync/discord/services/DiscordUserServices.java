package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordUserRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiscordUserServices {

    private final  DiscordApiServices discordApiServices;
    private final DiscordUserRepository discordUserRepository;

    public DiscordUserServices(DiscordApiServices discordApiServices, DiscordUserRepository discordUserRepository) {
        this.discordApiServices = discordApiServices;
        this.discordUserRepository = discordUserRepository;
    }

    private List<DiscordUser> membersToDiscordUsers(List<Member> members){
        List<DiscordUser> users = new ArrayList<>();
        long botId = discordApiServices.getBotId();

        for (Member member: members){
            if (member.getIdLong() != botId){
                users.add(new DiscordUser(member.getIdLong(), member.getUser().getEffectiveName()));
            }
        }
        return users;
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

    public void updateTable() {
        List<Member> members = discordApiServices.getMembers();
        discordUserRepository.saveAll(membersToDiscordUsers(members));
    }
}
