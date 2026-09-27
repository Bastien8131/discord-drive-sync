package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.repository.CoreLinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoreLinkServices {

    private final CoreLinkRepository coreLinkRepository;

    public CoreLinkServices(CoreLinkRepository coreLinkRepository) {
        this.coreLinkRepository = coreLinkRepository;
    }

    public boolean exists(String link){
        return coreLinkRepository.existsByUrl(link);
    }

    public Optional<CoreLink> findByUrl(String url){
        return coreLinkRepository.findByUrl(url);
    }

    public List<CoreLink> getOrCreateLinks(String text, CoreContent coreContent) {
        List<CoreLink> coreLinks = new ArrayList<>();

        for (String link : TextUtils.getLinkFromContent(text)){
            coreLinks.add(findByUrl(link).map(coreLink -> {
                coreLink.addCoreContent(coreContent);
                return coreLink;
            }).orElseGet(() -> {
                CoreLink coreLink = new CoreLink(link, coreContent);
                coreLinkRepository.save(coreLink);
                return coreLink;
            }));
        }

        return coreLinks;
    }
}
