package fr.bastienbories.discorddrivesync.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import net.dv8tion.jda.api.entities.Message.Attachment;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@Transactional
public class S3SyncServices {

    private static final Logger log = LoggerFactory.getLogger(S3SyncServices.class);

    private final S3AsyncClient s3AsyncClient;

    private final DriveFileServices driveFileServices;

    public S3SyncServices(S3AsyncClient s3AsyncClient, DriveFileServices driveFileServices) {
        this.s3AsyncClient = s3AsyncClient;
        this.driveFileServices = driveFileServices;
    }

    public CompletableFuture<List<DriveFile>> uploadMultipartFiles(List<MultipartFile> multipartFiles, DiscordUser uploader){

        List<CompletableFuture<Optional<DriveFile>>> futureDriveFiles = multipartFilesToFutureDriveFiles(multipartFiles, uploader);
        CompletableFuture<List<DriveFile>> driveFiles = saveSucceeded(futureDriveFiles);

        return driveFiles;
    }

    public CompletableFuture<List<DriveFile>> uploadAttachments(List<Attachment> attachments, DiscordUser uploader){

        List<CompletableFuture<Optional<DriveFile>>> futureDriveFiles = attachmentsToFutureDriveFiles(attachments, uploader);
        CompletableFuture<List<DriveFile>> driveFiles = saveSucceeded(futureDriveFiles);

        return driveFiles;
    }

    private CompletableFuture<List<DriveFile>> saveSucceeded(List<CompletableFuture<Optional<DriveFile>>> driveFiles){
        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(
                driveFiles.toArray(new CompletableFuture[driveFiles.size()])
        );

        return allFuturesResult.thenApply(v -> {
            List<DriveFile> files = driveFiles.stream().map(CompletableFuture::join).flatMap(Optional::stream).collect(Collectors.<DriveFile>toList());
            return driveFileServices.saveAll(files);
        });
    }

    private List<CompletableFuture<Optional<DriveFile>>> multipartFilesToFutureDriveFiles(List<MultipartFile> multipartFiles, DiscordUser uploader){
        List<CompletableFuture<Optional<DriveFile>>> driveFiles = new ArrayList<>();

        for (MultipartFile multipartFile: multipartFiles){
            if(!multipartFile.isEmpty()){
                driveFiles.add(uploadAndCreateDriveFile(
                    FileToUpload.fromMultipartFile(multipartFile),
                    uploader
                ));
            }
        }

        return driveFiles;
    }

    private List<CompletableFuture<Optional<DriveFile>>> attachmentsToFutureDriveFiles(List<Attachment> attachments, DiscordUser uploader){
        List<CompletableFuture<Optional<DriveFile>>> driveFiles = new ArrayList<>();

        for (Attachment attachment: attachments){
            driveFiles.add(uploadAndCreateDriveFile(
                FileToUpload.fromAttachment(attachment),
                uploader
            ));
        }

        return driveFiles;
    }

    private CompletableFuture<Optional<DriveFile>> uploadAndCreateDriveFile(FileToUpload file, DiscordUser uploader){

        String contentType = Objects.toString(file.contentType(), "application/octet-stream");
        String key = TextUtils.generateUUID();

        CompletableFuture<Optional<DriveFile>> driveFile = file.inputStream().thenCompose(inputStream -> {
            return putObject(inputStream, contentType, key);
        }).thenApply(putObjectResponse -> Optional.of(
            driveFileServices.create(file.discordId(), file.filename(), key, uploader)
        )).exceptionally(ex -> {
            LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
            return Optional.empty();
        });

        return driveFile;
    }

    private CompletableFuture<PutObjectResponse> putObject(InputStream inputStream, String contentType, String key){
        try (inputStream) {
            return s3AsyncClient.putObject(req -> req
                            .bucket("discord-drive-sync")
                            .key(key)
                            .contentType(contentType)
                            .build(),
                    AsyncRequestBody.fromBytes(inputStream.readAllBytes())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<DeleteObjectResponse> delete(DriveFile driveFile) {
        return s3AsyncClient.deleteObject(req -> req.bucket("discord-drive-sync").key(driveFile.getStorageKey()));
    }

    public CompletableFuture<List<DeleteObjectResponse>> deleteAll(List<DriveFile> driveFiles) {
        List<CompletableFuture<DeleteObjectResponse>> deleteObjs = new ArrayList<>();

        for (DriveFile driveFile : driveFiles) {
            CompletableFuture<DeleteObjectResponse> deleteObject = s3AsyncClient.deleteObject(req -> req.bucket("discord-drive-sync").key(driveFile.getStorageKey()).build())
                    .thenApply(deleteObjectResponse -> {
                try {
                    driveFileServices.delete(driveFile);
                } catch (OptimisticLockingFailureException ex) {
                    LogMessages.unexpectedError(log, ex);
                }
                return deleteObjectResponse;
            }).exceptionally(ex -> {
                LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
                return null;
            });
            deleteObjs.add(deleteObject);
        }

        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(
                deleteObjs.toArray(new CompletableFuture[deleteObjs.size()])
        );

        return allFuturesResult.thenApply(v -> {
            return deleteObjs.stream().map(CompletableFuture::join).collect(Collectors.<DeleteObjectResponse>toList());
        });
    }
}
