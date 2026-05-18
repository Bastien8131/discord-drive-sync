package fr.bastienbories.discorddrivesync;

import fr.bastienbories.discorddrivesync.core.services.CoreServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordBotServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final CoreServices coreServices;
    private final DiscordBotServices discordBotServices;

    public Runner(CoreServices coreServices, DiscordBotServices discordBotServices) {
        this.coreServices = coreServices;
        this.discordBotServices = discordBotServices;
    }

    @Override
    public void run(String... args) throws Exception {
//        coreServices.syncUsers();
//        System.out.println(discordBotServices.test());
    }
}
