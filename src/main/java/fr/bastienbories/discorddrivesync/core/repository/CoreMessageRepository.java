package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreMessageRepository extends JpaRepository<CoreMessage, Long> {
}
