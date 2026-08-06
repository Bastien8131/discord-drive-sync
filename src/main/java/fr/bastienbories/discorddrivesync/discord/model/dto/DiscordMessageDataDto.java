package fr.bastienbories.discorddrivesync.discord.model.dto;

import fr.bastienbories.discorddrivesync.core.model.dto.CoreLabelDto;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreLinkDto;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileDto;

import java.util.List;

public record DiscordMessageDataDto(
        String content,
        Long authorId,
        List<CoreLinkDto> links,
        List<CoreLabelDto> labels,
        List<DriveFileDto> files
) {
}
