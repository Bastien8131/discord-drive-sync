package fr.bastienbories.discorddrivesync.core.model.dto;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileDto;

import java.util.List;

public record CoreContentDto(
        long id,
        String text,
        Long authorId,
        List<Long> reposterIds,
        List<Long> discordMessageIds,
        List<CoreLinkDto> links,
        List<CoreLabelDto> labels,
        List<DriveFileDto> files
) {
}
