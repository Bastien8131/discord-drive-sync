package fr.bastienbories.discorddrivesync.drive.services;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.repository.DriveFileRepository;
import fr.bastienbories.discorddrivesync.sync.S3SyncServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Transactional
public class DriveFileServices {

    private static final Logger log = LoggerFactory.getLogger(S3SyncServices.class);

    private final S3AsyncClient s3AsyncClient;
    private final ExecutorService executorService;

    private final DriveFileRepository driveFileRepository;

    public DriveFileServices(S3AsyncClient s3AsyncClient, ExecutorService executorService, DriveFileRepository driveFileRepository) {
        this.s3AsyncClient = s3AsyncClient;
        this.executorService = executorService;
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
                .key(driveFile.getPath())
                .build(),
                AsyncResponseTransformer.toBlockingInputStream()
        );
    }

    public Optional<List<DriveFile>> findByCoreMessage(CoreMessage coreMessage) {
        return driveFileRepository.findByCoreMessagesContaining(coreMessage);
    }

    public Optional<List<DriveFile>> findByContainingOnlyThisCoreMessage(CoreMessage coreMessage){
        return driveFileRepository.findByContainingOnlyThisCoreMessage(coreMessage);
    }

    public void delete(DriveFile driveFile) {
        driveFileRepository.delete(driveFile);
    }
}
