package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;

public class CoreContentNotFoundException extends ResourceIdNotFoundException {
    public CoreContentNotFoundException(long id) {
        super(CoreContent.class, id);
    }
}
