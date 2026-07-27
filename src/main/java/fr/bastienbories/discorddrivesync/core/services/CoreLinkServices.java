package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.repository.CoreLinkRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
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

    public List<CoreLink> getOrCreateLinks(String content, DiscordMessageData discordMessageData) {
        List<CoreLink> coreLinks = new ArrayList<>();

        for (String link : TextUtils.getLinkFromContent(content)){
            coreLinks.add(findByUrl(link).map(coreLink -> {
                coreLink.addDiscordMessageData(discordMessageData);
                return coreLink;
            }).orElseGet(() -> {
                CoreLink coreLink = new CoreLink(link, discordMessageData);
                coreLinkRepository.save(coreLink);
                return coreLink;
            }));
        }

        return coreLinks;
    }
}
