package fr.bastienbories.discorddrivesync.sync;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.bastienbories.discorddrivesync.common.PublicRoute;
import fr.bastienbories.discorddrivesync.common.UrlServices;
import fr.bastienbories.discorddrivesync.core.model.CoreUploadLink;
import fr.bastienbories.discorddrivesync.core.services.CoreUploadLinkServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Service
@Transactional
public class SlashCommandSyncServices {

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

        discordUserServices.getById(userId).ifPresentOrElse(user -> {
            discordChannelServices.getById(channelId).ifPresentOrElse(channel -> {

                CoreUploadLink link = new CoreUploadLink(user, channel);
                coreUploadLinkServices.save(link);

                String url = publicUrlServices.buildUrl(PublicRoute.UPLOAD_FILE, "", link.getToken());
                System.out.print(url);
            }, () -> {});
        }, () -> {});
    }
}
