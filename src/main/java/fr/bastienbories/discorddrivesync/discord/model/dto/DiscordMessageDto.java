package fr.bastienbories.discorddrivesync.discord.model.dto;

public record DiscordMessageDto(
        long id,
        long contentId,
        long authorId,
        long discordChannelId
) {
}
