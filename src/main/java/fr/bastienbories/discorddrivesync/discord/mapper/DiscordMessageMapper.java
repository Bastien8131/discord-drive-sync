package fr.bastienbories.discorddrivesync.discord.mapper;

import fr.bastienbories.discorddrivesync.core.mapper.CoreContentMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CoreContentMapper.class,
        DiscordUserMapper.class
})
public interface DiscordMessageMapper {
    @Mapping(target = "discordChannelId", source = "discordChannel.id")
    @Mapping(target = "contentId", source = "content.id")
    @Mapping(target = "authorId", source = "author.id")
    DiscordMessageDto discordMessageToDiscordMessageDto(DiscordMessage discordMessage);

    List<DiscordMessageDto> discordMessageListToDiscordMessageDtoList(List<DiscordMessage> discordMessages);
}
