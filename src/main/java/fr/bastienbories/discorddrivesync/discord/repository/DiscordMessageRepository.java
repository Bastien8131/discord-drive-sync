package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordMessageRepository extends JpaRepository<DiscordMessage, Long> {
}
