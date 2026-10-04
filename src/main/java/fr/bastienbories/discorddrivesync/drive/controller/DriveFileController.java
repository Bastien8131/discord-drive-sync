package fr.bastienbories.discorddrivesync.drive.controller;

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

@RestController
public class DriveFileController {

    private final DriveFileServices driveFileServices;
    private final CoreUploadLinkServices coreUploadLinkServices;
    private final CoreLabelServices coreLabelServices;

    private final DiscordMessageServices discordMessageServices;

    public DriveFileController(DriveFileServices driveFileServices, CoreUploadLinkServices coreUploadLinkServices, CoreLabelServices coreLabelServices, DiscordMessageServices discordMessageServices) {
        this.driveFileServices = driveFileServices;
        this.coreUploadLinkServices = coreUploadLinkServices;
        this.coreLabelServices = coreLabelServices;
        this.discordMessageServices = discordMessageServices;
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
            List<DiscordChannel> channels = new ArrayList<>();

            for (Long id : labelIds) {
                coreLabelServices.getById(id).ifPresent(label -> {
                    labels.add(label);
                });
            }

            if(labels.size() <= 0){
                labels.add(link.getDiscordChannel().getLabel());
            }

            for (CoreLabel label : labels) {
                channels.addAll(label.getDiscordChannels());
            }

            //cree des driveFiles a partir files

            //cree des links(shareLink) a partir de driveFiles

            // files.get(0).getResource().

            //utiliser coreContentServices.getOrCreateAndAddDriveFiles
            CoreContent content = new CoreContent(description, user);
            //add messages
            //add links
            content.addLabelList(labels);
            //add files


            // Plustard a mettre dans une boucle pour crée des de nouveau message sur discord, les recup et crée des DiscordMessage.
            // DiscordMessage message = new DiscordMessage(0, content, user, link.getDiscordChannel());

        });

        System.out.print(description);
    }
}
