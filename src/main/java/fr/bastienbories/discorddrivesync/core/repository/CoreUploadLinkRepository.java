package fr.bastienbories.discorddrivesync.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;

public interface CoreUploadLinkRepository extends JpaRepository<CoreUploadLink, Long> {

    Optional<CoreUploadLink> findByToken(String token);

    @EntityGraph (attributePaths = {"user", "discordChannel"})
    Optional<CoreUploadLink> findFullByToken(String token);

}
