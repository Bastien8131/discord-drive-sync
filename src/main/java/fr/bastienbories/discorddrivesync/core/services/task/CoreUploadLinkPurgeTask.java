package fr.bastienbories.discorddrivesync.core.services.task;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fr.bastienbories.discorddrivesync.core.repository.CoreUploadLinkRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CoreUploadLinkPurgeTask {

    private final CoreUploadLinkRepository coreUploadLinkRepository;

    public CoreUploadLinkPurgeTask(CoreUploadLinkRepository coreUploadLinkRepository) {
        this.coreUploadLinkRepository = coreUploadLinkRepository;
    }

    @Scheduled(cron = "${purge.uploadlink.cron.expression}")
    public void purgeExpired() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        coreUploadLinkRepository.deleteAllByEndedAtLessThanEqual(now);
    }
}
