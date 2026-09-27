package fr.bastienbories.discorddrivesync.common;

import org.mapstruct.Named;

public interface EntityReferenceMapper {

    @Named("toId")
    public static <T extends HasId> Long toId(T entity) {
        return entity.getId();
    }

    @Named("toName")
    public static <T extends HasIdAndName> String toName(T entity) {
        return entity.getName();
    }
}
