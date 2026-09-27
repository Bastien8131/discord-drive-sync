package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordUserRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiscordUserServices {

    private final DiscordApiServices discordApiServices;
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
                users.add(new DiscordUser(member));
            }
        }
        return users;
    }

    public void updateTable() {
        List<Member> members = discordApiServices.getMembers();
        discordUserRepository.saveAll(membersToDiscordUsers(members));
    }

    public Optional<DiscordUser> getById(long id){
        return discordUserRepository.findById(id);
    }

    public Optional<DiscordUser> getOrFetchById(long id) {
        Optional<DiscordUser> user = getById(id);
        if (user.isEmpty()) {
            updateTable();
            return getById(id);
        }
        return user;
    }

    public List<DiscordUser> getAll() {
        return discordUserRepository.findAll();
    }
}
