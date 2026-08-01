package fr.bastienbories.discorddrivesync.drive.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriveFileRepository extends JpaRepository<DriveFile, Long> {
    Optional<DriveFile> findDriveFileByShareToken(String shareToken);

    Optional<List<DriveFile>> findByDiscordMessageDataListContaining(DiscordMessageData discordMessageData);
}
