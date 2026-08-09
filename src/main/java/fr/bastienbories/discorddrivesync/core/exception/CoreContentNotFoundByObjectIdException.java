package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;

public class CoreContentNotFoundByObjectIdException extends ResourceIdNotFoundException {
    public CoreContentNotFoundByObjectIdException(Class<?> relatedEntityClass, long id) {
        super(CoreContent.class, relatedEntityClass.getSimpleName() + " id", id);
    }
}
