package fr.bastienbories.discorddrivesync.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreUploadLinkDto;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordChannelMapper;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordUserMapper;

@Mapper(componentModel = "spring", uses = {
        DiscordUserMapper.class,
        DiscordChannelMapper.class
})
public interface CoreUploadLinkMapper {
    @Mapping(target = "channel", source = "discordChannel")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.repostedContentIds", ignore = true)
    CoreUploadLinkDto coreUploadLinkToCoreUploadLinkDto(CoreUploadLink coreUploadLink);
}