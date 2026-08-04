package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;

public class DiscordUserNotFoundException extends ResourceIdNotFoundException {
    public DiscordUserNotFoundException(long id) {
        super(DiscordUser.class, id);
    }
}
