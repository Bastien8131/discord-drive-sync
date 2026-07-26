package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordMessageDataRepository extends JpaRepository<DiscordMessageData, Long> {
    boolean existsByContent(String contentDisplay);

    DiscordMessageData findByContent(String content);
}
