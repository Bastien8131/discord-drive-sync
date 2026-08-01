package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreMessageDto;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordMessageDataMapper;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordUserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        CoreLabelMapper.class,
        DiscordMessageDataMapper.class,
        DiscordUserMapper.class
})
public interface CoreMessageMapper {
    CoreMessageDto coreMessageToCoreMessageDto(CoreMessage coreMessage);
}
