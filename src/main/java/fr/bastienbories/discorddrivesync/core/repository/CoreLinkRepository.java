package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreLinkRepository extends JpaRepository<CoreLink, Long> {
}
