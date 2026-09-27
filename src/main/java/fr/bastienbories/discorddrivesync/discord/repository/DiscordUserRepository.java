package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long> {
    List<DiscordUser> findByName(String name);
}
