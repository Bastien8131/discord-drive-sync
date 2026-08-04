package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;

public class CoreLabelIdNotFoundException extends ResourceIdNotFoundException {
    public CoreLabelIdNotFoundException(long id) {
        super(CoreLabel.class, id);
    }
}
