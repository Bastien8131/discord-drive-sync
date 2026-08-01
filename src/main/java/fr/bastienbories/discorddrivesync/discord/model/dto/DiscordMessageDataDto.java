package fr.bastienbories.discorddrivesync.discord.model.dto;

import fr.bastienbories.discorddrivesync.core.model.dto.CoreLinkDto;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileDto;

import java.util.List;

public record DiscordMessageDataDto(
        String content,
        List<CoreLinkDto> coreLinks,
        List<DriveFileDto> driveFiles
) {
}
