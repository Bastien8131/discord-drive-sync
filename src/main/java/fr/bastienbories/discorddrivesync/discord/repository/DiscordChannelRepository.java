package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscordChannelRepository extends JpaRepository<DiscordChannel, Long> {
    List<DiscordChannel> getAllByLabel(CoreLabel coreLabel);
}
