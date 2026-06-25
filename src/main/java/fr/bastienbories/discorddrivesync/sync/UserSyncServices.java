package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserSyncServices {

    private static final Logger log = LoggerFactory.getLogger(UserSyncServices.class);

    private final DiscordUserServices discordUserServices;

    public UserSyncServices(DiscordUserServices discordUserServices) {
        this.discordUserServices = discordUserServices;
    }

    public void updateUserTable() {
        discordUserServices.updateTable();
    }
}
