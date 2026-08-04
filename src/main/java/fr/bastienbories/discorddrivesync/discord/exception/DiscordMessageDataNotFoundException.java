package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;

public class DiscordMessageDataNotFoundException extends ResourceIdNotFoundException {
    public DiscordMessageDataNotFoundException(long id) {
        super(DiscordMessageData.class, id);
    }
}
