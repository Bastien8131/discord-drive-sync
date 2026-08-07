package fr.bastienbories.discorddrivesync.discord.mapper;

import fr.bastienbories.discorddrivesync.common.EntityReferenceMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscordUserMapper extends EntityReferenceMapper {
    @Mapping(target = "repostedContentIds", source = "repostedContents", qualifiedByName = "toId")
    DiscordUserDto discordUserToDiscordUserDto(DiscordUser discordUser);

    List<DiscordUserDto> discordUserListToDiscordUserDtoList(List<DiscordUser> discordUsers);
}
