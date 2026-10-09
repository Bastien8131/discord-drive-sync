package fr.bastienbories.discorddrivesync.drive.controller;

import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.bastienbories.discorddrivesync.common.PublicRoute;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import fr.bastienbories.discorddrivesync.core.services.CoreUploadLinkServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.DiscordMessageServices;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;
import fr.bastienbories.discorddrivesync.sync.MessageSyncServices;
import fr.bastienbories.discorddrivesync.sync.S3SyncServices;

@RestController
public class DriveFileController {

    private final DiscordChannelServices discordChannelServices;
    private final DriveFileServices driveFileServices;
    private final CoreUploadLinkServices coreUploadLinkServices;
    private final CoreLabelServices coreLabelServices;
    private final DiscordMessageServices discordMessageServices;

    private final S3SyncServices s3SyncServices;
    private final MessageSyncServices messageSyncServices;

    public DriveFileController(DriveFileServices driveFileServices, CoreUploadLinkServices coreUploadLinkServices, CoreLabelServices coreLabelServices, DiscordMessageServices discordMessageServices, S3SyncServices s3SyncServices, MessageSyncServices messageSyncServices, DiscordChannelServices discordChannelServices) {
        this.driveFileServices = driveFileServices;
        this.coreUploadLinkServices = coreUploadLinkServices;
        this.coreLabelServices = coreLabelServices;
        this.discordMessageServices = discordMessageServices;
        this.s3SyncServices = s3SyncServices;
        this.messageSyncServices = messageSyncServices;
        this.discordChannelServices = discordChannelServices;
    }

    @PostMapping(PublicRoute.Paths.UPLOAD_FILES + "/{token}")
    public void uploadFiles(
        @RequestParam List<Long> labelIds,
        @RequestParam String description,
        @RequestPart List<MultipartFile> files,
        @PathVariable String token
    ) {
        List<CoreLabel> labels = new ArrayList<>();

        coreUploadLinkServices.getByToken(token).ifPresent(link -> {

            DiscordUser user = link.getUser();
            List<DiscordChannel> targetChannels = new ArrayList<>();

            for (Long id : labelIds) {
                coreLabelServices.getById(id).ifPresent(label -> {
                    labels.add(label);
                });
            }

            if(labels.size() <= 0){
                labels.add(link.getDiscordChannel().getLabel());
            }

            for (CoreLabel label : labels) {
                targetChannels.addAll(discordChannelServices.getAllByLabel(label));
            }

            s3SyncServices.uploadMultipartFiles(files, user).thenAccept(driveFiles -> {
                messageSyncServices.createDiscordMessage(user, description, targetChannels, driveFiles);
            });

        });
    }
}
