package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;

public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(long id) {
        super(CoreCategory.class, id);
    }
}
