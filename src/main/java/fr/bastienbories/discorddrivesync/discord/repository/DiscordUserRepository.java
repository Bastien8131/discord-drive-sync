package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long> {
    List<DiscordUser> findByName(String name);
}
