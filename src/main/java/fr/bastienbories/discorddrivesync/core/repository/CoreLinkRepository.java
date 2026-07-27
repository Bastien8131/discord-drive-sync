package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoreLinkRepository extends JpaRepository<CoreLink, Long> {
    boolean existsByUrl(String url);

    Optional<CoreLink> findByUrl(String url);
}
