package fr.bastienbories.discorddrivesync.drive.services;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.repository.DriveFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DriveFileServices {

    private final DriveFileRepository driveFileRepository;


    public DriveFileServices(DriveFileRepository driveFileRepository) {
        this.driveFileRepository = driveFileRepository;
    }

    public List<DriveFile> saveAll(List<DriveFile> files) {
        return driveFileRepository.saveAll(files);
    }
}
