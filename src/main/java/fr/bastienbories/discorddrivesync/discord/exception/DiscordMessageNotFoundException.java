package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;

public class DiscordMessageNotFoundException extends ResourceIdNotFoundException {

    public DiscordMessageNotFoundException(long id) {
        super(DiscordMessage.class, id);
    }
}
