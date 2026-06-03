package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long> {
    Optional<DiscordUser> findByIdDiscordUser(long idDiscordUser);
    List<DiscordUser> findByName(String name);
}
