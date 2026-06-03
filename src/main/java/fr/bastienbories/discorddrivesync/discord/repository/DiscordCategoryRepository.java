package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscordCategoryRepository extends JpaRepository<DiscordCategory, Long> {
    Optional<DiscordCategory> findByName(String name);

    DiscordCategory getDiscordCategoryByName(String name);
}
