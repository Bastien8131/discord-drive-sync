package fr.bastienbories.discorddrivesync.drive.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface DriveFileRepository extends JpaRepository<DriveFile, Long> {
    Optional<DriveFile> findDriveFileByShareToken(String shareToken);

    Optional<List<DriveFile>> findByAttachedContentsContaining(CoreContent coreContent);
}
