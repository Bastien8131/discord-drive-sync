package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoreCategoryRepository extends JpaRepository<CoreCategory, Long> {
    Optional<CoreCategory> findCoreCategoryByDiscordCategory_Id(long id);

    @Query("SELECT c from CoreCategory c join fetch c.labels where c.idCategory = :id")
    Optional<CoreCategory> findByIdWithLabels(@Param("id") long id);
}
