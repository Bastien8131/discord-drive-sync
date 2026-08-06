package fr.bastienbories.discorddrivesync.core.model.dto;

import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileDto;

import java.util.List;

public record CoreContentDto(
        String text,
        Long authorId,
        List<CoreLinkDto> links,
        List<CoreLabelDto> labels,
        List<DriveFileDto> files
) {
}
