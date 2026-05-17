package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.discord.services.DiscordBotServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import org.springframework.stereotype.Service;

@Service
public class CoreServices {

    private final DiscordUserServices discordUserServices;
    private final DiscordBotServices discordBotServices;

    public CoreServices(DiscordUserServices discordUserServices, DiscordBotServices discordBotServices) {
        this.discordUserServices = discordUserServices;
        this.discordBotServices = discordBotServices;
    }

    public void syncUsers(){
        discordUserServices.addUsers(discordBotServices.getMembers());
    }
}
