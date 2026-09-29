package fr.bastienbories.discorddrivesync.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;

public interface CoreUploadLinkRepository extends JpaRepository<CoreUploadLink, Long> {

}
