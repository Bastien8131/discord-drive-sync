package fr.bastienbories.discorddrivesync.sync;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Service
@Transactional
public class SlashCommandSyncServices {

    private final DiscordUserServices discordUserServices;

    public SlashCommandSyncServices(DiscordUserServices discordUserServices){
        this.discordUserServices = discordUserServices;
    }

    public void uploadCommand(SlashCommandInteractionEvent event) {
        long summonerId = event.getUser().getIdLong();
        long channelId = event.getChannelIdLong();
    }
}
