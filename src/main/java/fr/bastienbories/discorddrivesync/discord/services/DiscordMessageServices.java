package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiscordMessageServices {

    public final DiscordMessageRepository discordMessageRepository;

    public DiscordMessageServices(DiscordMessageRepository discordMessageRepository) {
        this.discordMessageRepository = discordMessageRepository;
    }


    public void save(DiscordMessage discordMessage) {
        discordMessageRepository.save(discordMessage);
    }
}
