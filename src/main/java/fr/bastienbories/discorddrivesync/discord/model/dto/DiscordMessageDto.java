package fr.bastienbories.discorddrivesync.discord.model.dto;

import fr.bastienbories.discorddrivesync.core.model.dto.CoreContentDto;

public record DiscordMessageDto(
        long id,
        CoreContentDto content,
        DiscordUserDto author,
        long discordChannelId
) {
}
