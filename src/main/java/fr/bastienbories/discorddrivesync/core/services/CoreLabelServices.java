package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.repository.CoreLabelRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoreLabelServices {

    private final CoreLabelRepository coreLabelRepository;

    public CoreLabelServices(CoreLabelRepository labelRepository) {
        this.coreLabelRepository = labelRepository;
    }

    public boolean existByName(String name){
        return coreLabelRepository.findByName(name).isPresent();
    }

    public CoreLabel getOrCreateLabelByName(String name) {
        return coreLabelRepository.findByName(name)
                .orElseGet(() -> coreLabelRepository.save(new CoreLabel(name)));
    }

    public CoreLabel createByDiscordChannel(DiscordChannel discordChannel) {
        CoreLabel coreLabel = new CoreLabel(discordChannel);
        return coreLabelRepository.save(coreLabel);
    }
}
