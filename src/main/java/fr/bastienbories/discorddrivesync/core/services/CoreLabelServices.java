package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.repository.CoreLabelRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public CoreLabel createByDiscordChannel(DiscordChannel discordChannel) {
        CoreLabel coreLabel = new CoreLabel(discordChannel);
        return coreLabelRepository.save(coreLabel);
    }

    public void addDiscordMessageData(long labelId, DiscordMessageData discordMessageData) {
        coreLabelRepository.findById(labelId).ifPresent(coreLabel -> coreLabel.addDiscordMessageData(discordMessageData));
    }

    public Optional<CoreLabel> getById(long id) {
        return coreLabelRepository.findById(id);
    }

    public List<CoreLabel> getAll() {
        return coreLabelRepository.findAll();
    }

    public Optional<CoreLabel> getByName(String name) {
        return coreLabelRepository.findByName(name);
    }

    public CoreLabel getOrCreateLabelByName(String name) {
        return getByName(name).orElseGet(() -> coreLabelRepository.save(new CoreLabel(name)));
    }
}
