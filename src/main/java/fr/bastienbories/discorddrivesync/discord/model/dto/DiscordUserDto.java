package fr.bastienbories.discorddrivesync.discord.model.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record DiscordUserDto(
        long id,
        String name,
        String globalName,
        String effectiveName,
        String nickname,
        String avatarUrl,
        OffsetDateTime accountCreatedAt,
        OffsetDateTime joinedAt,
        int colorRaw,
        List<Long> repostedContentIds,
        boolean isBot,
        boolean isOwner,
        boolean isPending
        ) {
}
