package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoreCategoryRepository extends JpaRepository<CoreCategory, Long> {
    CoreCategory getCoreCategoryByDiscordCategory_IdDiscCategory(long discordCategoryIdDiscCategory);

    Optional<CoreCategory> findCoreCategoryByDiscordCategory_IdDiscCategory(long discordCategoryIdDiscCategory);
}
