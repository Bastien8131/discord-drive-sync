package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserSyncServices {

    private final DiscordUserServices discordUserServices;

    public UserSyncServices(DiscordUserServices discordUserServices) {
        this.discordUserServices = discordUserServices;
    }

    public void updateUserTable() {
        discordUserServices.updateTable();
    }
}
