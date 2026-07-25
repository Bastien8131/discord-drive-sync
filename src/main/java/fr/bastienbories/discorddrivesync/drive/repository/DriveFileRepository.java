package fr.bastienbories.discorddrivesync.drive.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriveFileRepository extends JpaRepository<DriveFile, Long> {
    Optional<DriveFile> findDriveFileByShareToken(String shareToken);

    Optional<List<DriveFile>> findByCoreMessagesContaining(CoreMessage coreMessage);

    @Query("select df from DriveFile df where SIZE(df.coreMessages) = 1 and :coreMessage member of df.coreMessages")
    Optional<List<DriveFile>> findByContainingOnlyThisCoreMessage(CoreMessage coreMessage);
}
