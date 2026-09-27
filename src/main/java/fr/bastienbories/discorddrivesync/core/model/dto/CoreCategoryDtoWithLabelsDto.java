package fr.bastienbories.discorddrivesync.core.model.dto;

import java.util.List;

public record CoreCategoryDtoWithLabelsDto(long id, String name, long discordCategoryId, List<CoreLabelDto> labels) {}
