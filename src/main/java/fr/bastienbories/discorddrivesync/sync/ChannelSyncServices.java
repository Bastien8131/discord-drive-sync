package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.services.CoreCategoryServices;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.juli.logging.Log;
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
    private final CoreCategoryServices coreCategoryServices;

    public ChannelSyncServices(DiscordCategoryServices discordCategoryServices, DiscordChannelServices discordChannelServices, CoreLabelServices coreLabelServices, CoreCategoryServices coreCategoryServices) {
        this.discordCategoryServices = discordCategoryServices;
        this.discordChannelServices = discordChannelServices;
        this.coreLabelServices = coreLabelServices;
        this.coreCategoryServices = coreCategoryServices;
    }

    public void createChannelFromDiscord(TextChannel channel){
        long idCategory = channel.getParentCategoryIdLong();
        String channelName = channel.getName();
        discordCategoryServices.getById(idCategory).ifPresentOrElse(
                discordCategory -> {
                    coreCategoryServices.getByDiscordId(discordCategory.getId()).ifPresentOrElse(coreCategory -> {
                        CoreLabel coreLabel = coreLabelServices.getOrCreateLabelByName(channelName.toLowerCase());
                        coreCategory.addCoreLabel(coreLabel);

                        DiscordChannel discordChannel = new DiscordChannel(
                                channel.getIdLong(),
                                channelName,
                                channel.getType(),
                                coreLabel,
                                discordCategory
                        );
                        discordChannelServices.save(discordChannel);
                    }, () -> {
                        LogMessages.notFoundInDatabase(log, CoreCategory.class, discordCategory.getId());
                    });
                },
                () -> {
                    LogMessages.notFoundInDatabase(log, DiscordCategory.class, idCategory);
                }
        );
    }

    public void deleteChannelFromDiscord(TextChannel channel){
        long id = channel.getIdLong();
        discordChannelServices.getById(id).ifPresentOrElse(
                discordChannelServices::delete,
                () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, id)
        );
    }
}
