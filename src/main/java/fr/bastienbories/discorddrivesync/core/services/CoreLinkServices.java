package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.repository.CoreLinkRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
@Transactional
public class CoreLinkServices {

    private final CoreLinkRepository coreLinkRepository;

    public CoreLinkServices(CoreLinkRepository coreLinkRepository) {
        this.coreLinkRepository = coreLinkRepository;
    }

    public List<CoreLink> createLinks(String content, DiscordMessageData discordMessageData) {
        List<CoreLink> coreLinks = new ArrayList<>();

        for (String link : TextUtils.getLinkFromContent(content)){
            CoreLink coreLink = new CoreLink(link, discordMessageData);
            coreLinks.add(coreLink);
        }

        coreLinkRepository.saveAll(coreLinks);
        return coreLinks;
    }
}
