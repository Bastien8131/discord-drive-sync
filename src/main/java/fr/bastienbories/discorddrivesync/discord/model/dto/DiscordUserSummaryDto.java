package fr.bastienbories.discorddrivesync.discord.model.dto;

import java.util.List;

public record DiscordUserSummaryDto(
        long id,
        String name,
        String globalName,
        String effectiveName,
        String nickname,
        String avatarUrl,
        List<Long> repostedContentIds
) {
}
