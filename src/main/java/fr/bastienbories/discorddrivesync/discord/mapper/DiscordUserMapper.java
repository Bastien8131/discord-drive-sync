package fr.bastienbories.discorddrivesync.discord.mapper;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscordUserMapper {
    DiscordUserDto discordUserToDiscordUserDto(DiscordUser discordUser);
}
