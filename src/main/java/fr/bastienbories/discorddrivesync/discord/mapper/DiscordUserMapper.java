package fr.bastienbories.discorddrivesync.discord.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.bastienbories.discorddrivesync.common.EntityReferenceMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserDto;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserSummaryDto;

@Mapper(componentModel = "spring")
public interface DiscordUserMapper extends EntityReferenceMapper {
    @Mapping(target = "repostedContentIds", source = "repostedContents", qualifiedByName = "toId")
    DiscordUserDto discordUserToDiscordUserDto(DiscordUser discordUser);

    List<DiscordUserSummaryDto> discordUserListToDiscordUserSummaryDtoList(List<DiscordUser> discordUsers);
}
