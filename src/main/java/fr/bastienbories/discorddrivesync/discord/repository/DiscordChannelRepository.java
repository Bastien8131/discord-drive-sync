package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscordChannelRepository extends JpaRepository<DiscordChannel, Long> {
    List<DiscordChannel> getAllByLabel(CoreLabel coreLabel);
}
