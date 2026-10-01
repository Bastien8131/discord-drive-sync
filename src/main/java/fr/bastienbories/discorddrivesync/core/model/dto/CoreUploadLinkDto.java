package fr.bastienbories.discorddrivesync.core.model.dto;

import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordChannelDto;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserSummaryDto;

public record CoreUploadLinkDto(
    long id,
    String token,
    boolean used,
    DiscordUserSummaryDto user,
    DiscordChannelDto channel
) {}
