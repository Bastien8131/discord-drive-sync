package fr.bastienbories.discorddrivesync.discord.mapper;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        DiscordMessageDataMapper.class,
        DiscordUserMapper.class
})
public interface DiscordMessageMapper {
    @Mapping(target = "discordChannelId", source = "discordChannel.id")
    DiscordMessageDto discordMessageToDiscordMessageDto(DiscordMessage discordMessage);
}
