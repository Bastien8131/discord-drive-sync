package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreCategoryDto;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreCategoryDtoWithLabelsDto;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreLabelDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CoreLabelMapper.class})
public interface CoreCategoryMapper {

    @Named("withoutLabels")
    @Mapping(target = "discordCategoryId", source = "discordCategory.id")
    @Mapping(target = "labels", expression = "java(java.util.Collections.emptyList())")
    CoreCategoryDtoWithLabelsDto coreCategoryToCoreCategoryDtoWithoutLabels(CoreCategory coreCategory);

    @IterableMapping(qualifiedByName = "withoutLabels")
    List<CoreCategoryDtoWithLabelsDto> coreCategoryListToCoreCategoryDtoListWithoutLabels(List<CoreCategory> coreCategories);

    @Named("withLabels")
    @Mapping(target = "discordCategoryId", source = "discordCategory.id")
    CoreCategoryDtoWithLabelsDto coreCategoryToCoreCategoryDtoWithLabelsDto(CoreCategory coreCategory);

    @IterableMapping(qualifiedByName = "withLabels")
    List<CoreCategoryDtoWithLabelsDto> coreCategoryListToCoreCategoryDtoListWithLabelsDto(List<CoreCategory> coreCategories);

}
