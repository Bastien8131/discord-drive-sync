package fr.bastienbories.discorddrivesync.discord.model.dto;

import fr.bastienbories.discorddrivesync.core.model.dto.CoreContentDto;

public record DiscordMessageDto(
        long id,
        long contentId,
        long authorId,
        long discordChannelId
) {
}
