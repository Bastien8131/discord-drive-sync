package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiscordChannelServices {

    public final DiscordChannelRepository discordChannelRepository;

    public DiscordChannelServices(DiscordChannelRepository discordChannelRepository) {
        this.discordChannelRepository = discordChannelRepository;
    }

    public void save(DiscordChannel discordChannel){
        discordChannelRepository.save(discordChannel);
    }

    public DiscordChannel getById(long idLong) {
        return discordChannelRepository.getReferenceById(idLong);
    }

    public void delete(DiscordChannel discordChannel) {
        discordChannelRepository.delete(discordChannel);
    }

    public List<DiscordChannel> getAllByLabel(CoreLabel coreLabel) {
        return discordChannelRepository.getAllByLabel(coreLabel);
    }
}
