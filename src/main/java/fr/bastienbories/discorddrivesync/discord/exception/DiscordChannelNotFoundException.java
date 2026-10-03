package fr.bastienbories.discorddrivesync.discord.exception;

import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;

public class DiscordChannelNotFoundException extends ResourceIdNotFoundException {
    public DiscordChannelNotFoundException(long id) {
        super(DiscordChannel.class, id);
    }
}
