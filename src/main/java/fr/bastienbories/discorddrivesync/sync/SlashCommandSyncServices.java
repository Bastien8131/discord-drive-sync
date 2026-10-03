package fr.bastienbories.discorddrivesync.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.common.PublicRoute;
import fr.bastienbories.discorddrivesync.common.ResourceIdNotFoundException;
import fr.bastienbories.discorddrivesync.common.UrlServices;
import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.services.CoreUploadLinkServices;
import fr.bastienbories.discorddrivesync.discord.exception.DiscordChannelNotFoundException;
import fr.bastienbories.discorddrivesync.discord.exception.DiscordUserNotFoundException;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Service
public class SlashCommandSyncServices {

    private static final Logger log = LoggerFactory.getLogger(SlashCommandSyncServices.class);

    private final UrlServices publicUrlServices;
    private final DiscordUserServices discordUserServices;
    private final DiscordChannelServices discordChannelServices;
    private final CoreUploadLinkServices coreUploadLinkServices;

    public SlashCommandSyncServices(DiscordUserServices discordUserServices,
            DiscordChannelServices discordChannelServices, CoreUploadLinkServices coreUploadLinkServices, UrlServices publicUrlServices) {
        this.publicUrlServices = publicUrlServices;
        this.discordUserServices = discordUserServices;
        this.discordChannelServices = discordChannelServices;
        this.coreUploadLinkServices = coreUploadLinkServices;
    }


    public void uploadCommand(SlashCommandInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        long channelId = event.getChannelIdLong();

        try {
            event.reply(getUploadLinkUrl(userId, channelId)).setEphemeral(true).queue();
        } catch (ResourceIdNotFoundException e) {
            LogMessages.notFoundInDatabase(log, e.getEntityClass(), e.getId());
            event.reply("A server-side problem occurred, please try again").setEphemeral(true).queue();
        }
    }

    private String getUploadLinkUrl(long userId, long channelId) {
        DiscordUser user = discordUserServices.getById(userId)
                .orElseThrow(() -> new DiscordUserNotFoundException(userId));
        DiscordChannel channel = discordChannelServices.getById(channelId)
                .orElseThrow(() -> new DiscordChannelNotFoundException(channelId));

        CoreUploadLink link = coreUploadLinkServices.create(user, channel);

        return publicUrlServices.buildUrl(PublicRoute.UPLOAD_FILE, "", link.getToken());
    }
}
