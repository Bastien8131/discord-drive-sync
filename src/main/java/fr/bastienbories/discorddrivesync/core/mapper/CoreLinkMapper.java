package fr.bastienbories.discorddrivesync.core.mapper;

import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreLinkDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoreLinkMapper {
    CoreLinkDto coreLinkToCoreLinkDto(CoreLink coreLink);
}
