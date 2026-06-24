package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CoreLabelMapper.class})
public interface CoreCategoryMapper {
    @Mapping(target = "discordCategoryId", source = "discordCategory.id")
    CoreCategoryDto coreCategoryToCoreCategoryDto(CoreCategory coreCategory);
}
