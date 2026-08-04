package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;

public class CategoryNotFoundException extends ResourceIdNotFoundException {

    public CategoryNotFoundException(long id) {
        super(CoreCategory.class, id);
    }
}
