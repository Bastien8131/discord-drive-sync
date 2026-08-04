package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;

public class DiscordMessageDataNotFoundException extends ResourceNotFoundException {
    public DiscordMessageDataNotFoundException(long id) {
        super(DiscordMessageData.class, id);
    }
}
