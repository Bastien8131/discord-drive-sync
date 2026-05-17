package fr.bastienbories.discorddrivesync;

import fr.bastienbories.discorddrivesync.core.services.CoreServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordBotServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscordDriveSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordDriveSyncApplication.class, args);
    }

}
