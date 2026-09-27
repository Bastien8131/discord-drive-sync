package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CoreLabelRepository extends JpaRepository<CoreLabel, Long> {
    Optional<CoreLabel> findByName(String name);
}
