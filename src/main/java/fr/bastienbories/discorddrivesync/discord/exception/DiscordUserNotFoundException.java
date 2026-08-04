package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;

public class DiscordUserNotFoundException extends ResourceNotFoundException {
    public DiscordUserNotFoundException(long id) {
        super(DiscordUser.class, id);
    }
}
