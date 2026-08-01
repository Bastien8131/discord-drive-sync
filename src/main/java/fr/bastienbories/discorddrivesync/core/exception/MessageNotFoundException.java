package fr.bastienbories.discorddrivesync.core.exception;

import fr.bastienbories.discorddrivesync.common.ResourceNotFoundException;
import fr.bastienbories.discorddrivesync.core.model.CoreMessage;

public class MessageNotFoundException extends ResourceNotFoundException {

    public MessageNotFoundException(long id) {
        super(CoreMessage.class, id);
    }
}
