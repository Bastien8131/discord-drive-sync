package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordMessageDataRepository;
import net.dv8tion.jda.api.entities.Message;
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

    public boolean dataAlreadyExists(Message message) {
        // In the future check other column
        return discordMessageDataRepository.existsByContent(message.getContentDisplay());
    }

    public DiscordMessageData getByContent(String contentDisplay) {
        return discordMessageDataRepository.getDiscordMessageDataByContent(contentDisplay);
    }
}
