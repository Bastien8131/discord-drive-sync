package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.core.repository.CoreMessageRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoreMessageServices {

    private final CoreMessageRepository coreMessageRepository;

    public CoreMessageServices(CoreMessageRepository coreMessageRepository) {
        this.coreMessageRepository = coreMessageRepository;
    }

    public void save(CoreMessage coreMessage) {
        coreMessageRepository.save(coreMessage);
    }
}
