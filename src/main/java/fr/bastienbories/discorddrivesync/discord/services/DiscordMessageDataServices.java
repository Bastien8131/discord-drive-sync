package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordMessageDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiscordMessageDataServices {

    private final DiscordMessageDataRepository discordMessageDataRepository;

    public DiscordMessageDataServices(DiscordMessageDataRepository discordMessageDataRepository) {
        this.discordMessageDataRepository = discordMessageDataRepository;
    }

    public void save(DiscordMessageData discordMessageData) {
        discordMessageDataRepository.save(discordMessageData);
    }
}
