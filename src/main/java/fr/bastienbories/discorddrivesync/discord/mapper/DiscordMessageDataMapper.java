package fr.bastienbories.discorddrivesync.discord.mapper;

import fr.bastienbories.discorddrivesync.core.mapper.CoreLinkMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDataDto;
import fr.bastienbories.discorddrivesync.drive.mapper.DriveFileMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        CoreLinkMapper.class,
        DriveFileMapper.class
})
public interface DiscordMessageDataMapper {
    DiscordMessageDataDto discordMessageDataToDiscordMessageDataDto(DiscordMessageData discordMessageData);
}
