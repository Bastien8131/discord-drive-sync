package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscordMessageRepository extends JpaRepository<DiscordMessage, Long> {
    List<DiscordMessage> getAllByDiscordMessageData(DiscordMessageData discordMessageData);

    boolean existsByDiscordMessageData(DiscordMessageData discordMessageData);
}
