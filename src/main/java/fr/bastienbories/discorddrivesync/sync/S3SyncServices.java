package fr.bastienbories.discorddrivesync.sync;

import com.google.common.io.Files;
import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;

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

    public CompletableFuture<List<DriveFile>> getFilesFromMessageAndUpload(Message message, DiscordUser discordUser) {
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
                    discordUser
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
}
