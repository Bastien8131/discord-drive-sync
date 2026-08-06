package fr.bastienbories.discorddrivesync.discord.model.dto;

public record DiscordMessageDto(
        long id,
        DiscordMessageDataDto discordMessageData,
        DiscordUserDto author,
        long discordChannelId
) {
}
