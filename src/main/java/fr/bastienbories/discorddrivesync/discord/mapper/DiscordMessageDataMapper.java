package fr.bastienbories.discorddrivesync.discord.mapper;

import fr.bastienbories.discorddrivesync.core.mapper.CoreLabelMapper;
import fr.bastienbories.discorddrivesync.core.mapper.CoreLinkMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDataDto;
import fr.bastienbories.discorddrivesync.drive.mapper.DriveFileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CoreLinkMapper.class,
        CoreLabelMapper.class,
        DriveFileMapper.class
})
public interface DiscordMessageDataMapper {
    @Mapping(target = "authorId", source = "author.id")
    DiscordMessageDataDto discordMessageDataToDiscordMessageDataDto(DiscordMessageData discordMessageData);

    List<DiscordMessageDataDto> discordMessageDataListToDiscordMessageDataDtoList(List<DiscordMessageData> discordMessageDataList);
}
