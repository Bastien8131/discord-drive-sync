package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;

public class DiscordMessageNotFoundException extends ResourceNotFoundException {

    public DiscordMessageNotFoundException(long id) {
        super(DiscordMessage.class, id);
    }
}
