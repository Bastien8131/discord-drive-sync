package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.discord.services.DiscordApiServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordBotServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoreServices {

    private final DiscordUserServices discordUserServices;
    private final DiscordBotServices discordBotServices;
    private final DiscordApiServices discordApiServices;

    public CoreServices(DiscordUserServices discordUserServices, DiscordBotServices discordBotServices, DiscordApiServices discordApiServices) {
        this.discordUserServices = discordUserServices;
        this.discordBotServices = discordBotServices;
        this.discordApiServices = discordApiServices;
    }
}
