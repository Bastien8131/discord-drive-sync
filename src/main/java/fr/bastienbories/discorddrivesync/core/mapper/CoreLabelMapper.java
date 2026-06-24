package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreLabelDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoreLabelMapper {
    CoreLabelDto coreLabelToCoreLabelDto(CoreLabel coreLabel);
}
