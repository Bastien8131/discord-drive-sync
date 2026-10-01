package fr.bastienbories.discorddrivesync.core.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.repository.CoreUploadLinkRepository;

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
}
