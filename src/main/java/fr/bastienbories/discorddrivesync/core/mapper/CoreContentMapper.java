package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreContentDto;
import fr.bastienbories.discorddrivesync.drive.mapper.DriveFileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CoreLinkMapper.class,
        CoreLabelMapper.class,
        DriveFileMapper.class
})
public interface CoreContentMapper {
    @Mapping(target = "authorId", source = "author.id")
    CoreContentDto coreContentToCoreContentDto(CoreContent coreContent);

    List<CoreContentDto> coreContentListToCoreContentDtoList(List<CoreContent> coreContentList);
}
