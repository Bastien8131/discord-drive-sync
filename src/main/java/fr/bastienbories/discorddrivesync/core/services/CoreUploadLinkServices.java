package fr.bastienbories.discorddrivesync.core.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.repository.CoreUploadLinkRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;

@Service
@Transactional
public class CoreUploadLinkServices {

    private final CoreUploadLinkRepository coreUploadLinkRepository;

    public CoreUploadLinkServices(CoreUploadLinkRepository coreUploadLinkRepository) {
        this.coreUploadLinkRepository = coreUploadLinkRepository;
    }

    public void save(CoreUploadLink link) {
        coreUploadLinkRepository.save(link);
    }

    public Optional<CoreUploadLink> getByToken(String token) {
        return coreUploadLinkRepository.findFullByToken(token);
    }

    public CoreUploadLink create(DiscordUser user, DiscordChannel channel){
        CoreUploadLink link = new CoreUploadLink(user, channel);
        save(link);
        return link;
    }
}
