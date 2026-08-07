package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.common.HasId;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreContentDto;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.drive.mapper.DriveFileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CoreLinkMapper.class,
        CoreLabelMapper.class,
        DriveFileMapper.class
})
public interface CoreContentMapper {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "reposterIds", source = "reposters", qualifiedByName = "toId")
    @Mapping(target = "discordMessageIds", source = "discordMessages", qualifiedByName = "toId")
    CoreContentDto coreContentToCoreContentDto(CoreContent coreContent);

    List<CoreContentDto> coreContentListToCoreContentDtoList(List<CoreContent> coreContentList);

    @Named("toId")
    public static <T extends HasId> Long toId(T entity) {
        return entity.getId();
    }

}
