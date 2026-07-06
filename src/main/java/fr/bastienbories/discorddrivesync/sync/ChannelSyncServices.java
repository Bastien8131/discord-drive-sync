package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ChannelSyncServices {

    private static final Logger log = LoggerFactory.getLogger(ChannelSyncServices.class);

    private final DiscordCategoryServices discordCategoryServices;
    private final DiscordChannelServices discordChannelServices;
    private final CoreLabelServices coreLabelServices;

    public ChannelSyncServices(DiscordCategoryServices discordCategoryServices, DiscordChannelServices discordChannelServices, CoreLabelServices coreLabelServices) {
        this.discordCategoryServices = discordCategoryServices;
        this.discordChannelServices = discordChannelServices;
        this.coreLabelServices = coreLabelServices;
    }

    public void createChannelFromDiscord(TextChannel channel){
        long idCategory = channel.getParentCategoryIdLong();
        Optional<DiscordCategory> discordCategory = discordCategoryServices.getById(idCategory);

        if (discordCategory.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordCategory.class, idCategory);
            return;
        }

        CoreLabel label = coreLabelServices.getOrCreateLabelByName(channel.getName().toLowerCase());

        DiscordChannel discordChannel = new DiscordChannel(
                channel.getIdLong(),
                channel.getName(),
                channel.getType(),
                label,
                discordCategory.get()
        );
        discordChannelServices.save(discordChannel);
    }

    public void deleteChannelFromDiscord(TextChannel channel){
        long id = channel.getIdLong();
        Optional<DiscordChannel> discordChannel = discordChannelServices.getById(id);

        if (discordChannel.isEmpty()) {
            LogMessages.notFoundInDatabase(log, DiscordChannel.class, id);
            return;
        }

        discordChannelServices.delete(discordChannel.get());
    }
}
