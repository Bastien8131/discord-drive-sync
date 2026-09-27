package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscordCategoryRepository extends JpaRepository<DiscordCategory, Long> {
    Optional<DiscordCategory> findByName(String name);

    DiscordCategory getDiscordCategoryByName(String name);
}
