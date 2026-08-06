package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@Transactional
public class S3SyncServices {

    private static final Logger log = LoggerFactory.getLogger(S3SyncServices.class);

    private final S3AsyncClient s3AsyncClient;
    private final ExecutorService executorService;

    private final DriveFileServices driveFileServices;

    public S3SyncServices(S3AsyncClient s3AsyncClient, ExecutorService executorService, DriveFileServices driveFileServices) {
        this.s3AsyncClient = s3AsyncClient;
        this.executorService = executorService;
        this.driveFileServices = driveFileServices;
    }

    private String getExtension(Attachment attachment){
        return Objects.toString(
                attachment.getFileExtension(),
                Objects.toString(attachment.getFileName(), "unknown")
        );
    }

    public CompletableFuture<List<DriveFile>> getFilesFromMessageAndUpload(Message message, DiscordUser uploader) {
        List<Attachment> attachments = message.getAttachments();
        List<CompletableFuture<Optional<DriveFile>>> driveFiles = new ArrayList<>();

        for (Attachment attachment: attachments){
            String contentType = Objects.toString(attachment.getContentType(), "unknown");
            String path = String.format("%s/%d", getExtension(attachment), attachment.getIdLong());
            CompletableFuture<Optional<DriveFile>> driveFile = attachment.getProxy().download().thenCompose(inputStream -> {
                try (inputStream) {
                    return s3AsyncClient.putObject(req -> req
                                    .bucket("discord-drive-sync")
                                    .key(path)
                                    .contentType(contentType)
                                    .build(),
                            AsyncRequestBody.fromBytes(inputStream.readAllBytes())
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).thenApply(putObjectResponse -> Optional.of(new DriveFile(
                    attachment.getIdLong(),
                    attachment.getFileName(),
                    path,
                    uploader
            ))).exceptionally(ex -> {
                LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
                return Optional.empty();
            });

            driveFiles.add(driveFile);
        }

        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(
                driveFiles.toArray(new CompletableFuture[driveFiles.size()])
        );

        return allFuturesResult.thenApply(v -> {
            List<DriveFile> files = driveFiles.stream().map(CompletableFuture::join).flatMap(Optional::stream).collect(Collectors.<DriveFile>toList());
            return driveFileServices.saveAll(files);
        });
    }

    public CompletableFuture<DeleteObjectResponse> delete(DriveFile driveFile) {
        return s3AsyncClient.deleteObject(req -> req.bucket("discord-drive-sync").key(driveFile.getStorageKey()));
    }

    public CompletableFuture<List<DeleteObjectResponse>> deleteMultipleFiles(List<DriveFile> driveFiles) {
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
