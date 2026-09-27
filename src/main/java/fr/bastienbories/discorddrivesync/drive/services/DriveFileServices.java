package fr.bastienbories.discorddrivesync.drive.services;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.repository.DriveFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class DriveFileServices {

    private final S3AsyncClient s3AsyncClient;

    private final DriveFileRepository driveFileRepository;

    public DriveFileServices(S3AsyncClient s3AsyncClient, DriveFileRepository driveFileRepository) {
        this.s3AsyncClient = s3AsyncClient;
        this.driveFileRepository = driveFileRepository;
    }

    public void deleteAll(List<DriveFile> driveFiles) {
        driveFileRepository.deleteAll(driveFiles);
    }

    public List<DriveFile> saveAll(List<DriveFile> files) {
        return driveFileRepository.saveAll(files);
    }

    public Optional<DriveFile> findByShareToken(String shareToken) {
        return driveFileRepository.findDriveFileByShareToken(shareToken);
    }

    public CompletableFuture<ResponseInputStream<GetObjectResponse>> downloadFile(DriveFile driveFile){
        return s3AsyncClient.getObject(req -> req
                .bucket("discord-drive-sync")
                .key(driveFile.getStorageKey())
                .build(),
                AsyncResponseTransformer.toBlockingInputStream()
        );
    }

    public Optional<List<DriveFile>> findByCoreContent(CoreContent coreContent) {
        return driveFileRepository.findByAttachedContentsContaining(coreContent);
    }

    public void delete(DriveFile driveFile) {
        driveFileRepository.delete(driveFile);
    }
}
