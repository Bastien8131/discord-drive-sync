package fr.bastienbories.discorddrivesync.core.services;

import org.springframework.stereotype.Service;

import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.repository.CoreUploadLinkRepository;
import jakarta.transaction.Transactional;

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
}
