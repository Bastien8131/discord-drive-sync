package fr.bastienbories.discorddrivesync.drive.controller;

import fr.bastienbories.discorddrivesync.discord.services.DiscordApiServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import fr.bastienbories.discorddrivesync.common.AsyncUtils;
import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.common.PublicRoute;
import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.services.CoreUploadLinkServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.mapper.DriveFileMapper;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileResultDto;
import fr.bastienbories.discorddrivesync.drive.result.DriveFileResult.Failure;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import fr.bastienbories.discorddrivesync.sync.MessageSyncServices;
import fr.bastienbories.discorddrivesync.sync.S3SyncServices;

@RestController
public class DriveFileController {

    private static final Logger log = LoggerFactory.getLogger(DriveFileController.class);

    private final DiscordChannelServices discordChannelServices;
    private final DriveFileServices driveFileServices;
    private final CoreUploadLinkServices coreUploadLinkServices;

    private final DiscordApiServices discordApiServices;
    private final S3SyncServices s3SyncServices;
    private final MessageSyncServices messageSyncServices;

    private final DriveFileMapper driveFileMapper;

    public DriveFileController(DriveFileServices driveFileServices, CoreUploadLinkServices coreUploadLinkServices, S3SyncServices s3SyncServices, DiscordApiServices discordApiServices, MessageSyncServices messageSyncServices, DiscordChannelServices discordChannelServices, DriveFileMapper driveFileMapper) {
        this.driveFileServices = driveFileServices;
        this.coreUploadLinkServices = coreUploadLinkServices;
        this.discordApiServices = discordApiServices;
        this.s3SyncServices = s3SyncServices;
        this.messageSyncServices = messageSyncServices;
        this.discordChannelServices = discordChannelServices;
        this.driveFileMapper = driveFileMapper;
    }

    @PostMapping(PublicRoute.Paths.UPLOAD_FILES + "/{token}")
    public CompletableFuture<DriveFileResultDto> uploadFiles(
        @RequestParam List<Long> labelIds,
        @RequestParam String description,
        @RequestPart List<MultipartFile> files,
        @PathVariable String token
    ) {

        if (files.isEmpty() || token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        CoreUploadLink link = coreUploadLinkServices.getByToken(token).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        if (link.isUsed()) {
            throw new ResponseStatusException(HttpStatus.LOCKED);
        }

        if (link.getEndedAt().isBefore(OffsetDateTime.now(ZoneOffset.UTC))){
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        coreUploadLinkServices.updateUsedStatus(link, true);

        DiscordUser user = link.getUser();
        List<DiscordChannel> targetChannels = new ArrayList<>();

        if(labelIds.isEmpty()){
            labelIds.add(link.getDiscordChannel().getLabel().getId());
        }

        for (Long id : labelIds) {
            targetChannels.addAll(discordChannelServices.getAllByLabelId(id));
        }

        return s3SyncServices.uploadMultipartFiles(files, user).thenCompose(results -> {

            if(!files.isEmpty() && results.isEmpty()){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<Failure> failures = driveFileServices.getFailedResult(results);
            List<DriveFile> drivefiles = driveFileServices.getSucceededResult(results);

            if(drivefiles.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            if(!failures.isEmpty()){
                discordApiServices.reportDriveFilesFailures(failures, link);
            }

            return messageSyncServices.createDiscordMessage(user, description, targetChannels, drivefiles).thenApply(v -> {
                return driveFileMapper.resultsToResultsDto(results);
            });
            
        }).exceptionally(ex -> {
            Throwable th = AsyncUtils.unwrap(ex);

            if (th instanceof ResponseStatusException rse) {
                throw rse;
            }
            
            LogMessages.unexpectedErrorDuringAsyncProcessing(log, th);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
