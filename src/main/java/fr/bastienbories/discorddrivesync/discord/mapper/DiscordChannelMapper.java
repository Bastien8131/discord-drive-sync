package fr.bastienbories.discorddrivesync.discord.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.bastienbories.discorddrivesync.core.mapper.CoreLabelMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordChannelDto;

@Mapper(componentModel = "spring", uses = {
        CoreLabelMapper.class,
        DiscordUserMapper.class
})
public interface DiscordChannelMapper {
    @Mapping (target = "labelId", source = "label.id")
    @Mapping (target = "discordCategoryId", source = "discordCategory.id")
    DiscordChannelDto discordChannelToDiscordChannelDto(DiscordChannel discordChannel);
}
