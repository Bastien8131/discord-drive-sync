package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceNameNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;

public class CoreLabelNameNotFoundException extends ResourceNameNotFoundException {

    public CoreLabelNameNotFoundException(String name) {
        super(CoreLabel.class, name);
    }
}
