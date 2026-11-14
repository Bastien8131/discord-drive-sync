package fr.bastienbories.discorddrivesync.discord.model.dto;

import net.dv8tion.jda.api.entities.channel.ChannelType;

public record DiscordChannelDto(
    long id,
    String name,
    ChannelType type,
    long labelId,
    long discordCategoryId
) {}
