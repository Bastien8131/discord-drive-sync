package fr.bastienbories.discorddrivesync.core.model.dto;

import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDataDto;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserDto;

import java.util.List;


public record CoreMessageDto(
        long id,
        DiscordUserDto discordUser,
        DiscordMessageDataDto discordMessageData,
        List<CoreLabelDto> coreLabels
) {
}
