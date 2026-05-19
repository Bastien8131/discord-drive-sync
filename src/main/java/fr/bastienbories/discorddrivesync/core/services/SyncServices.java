package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SyncServices {

    private final DiscordCategoryServices discordCategoryServices;
    private final DiscordChannelServices discordChannelServices;

    private final CoreCategoryServices coreCategoryServices;
    private final CoreLabelServices coreLabelServices;
    private final DiscordUserServices discordUserServices;

    public SyncServices(DiscordCategoryServices discordCategoryServices, DiscordChannelServices discordChannelServices, CoreCategoryServices coreCategoryServices, CoreLabelServices coreLabelServices, DiscordUserServices discordUserServices) {
        this.discordCategoryServices = discordCategoryServices;
        this.discordChannelServices = discordChannelServices;
        this.coreCategoryServices = coreCategoryServices;
        this.coreLabelServices = coreLabelServices;
        this.discordUserServices = discordUserServices;
    }

    //---Category

    public void createDiscordCategory(Category channel){
        DiscordCategory discordCategory = new DiscordCategory(channel.getIdLong(), channel.getName());
        CoreCategory coreCategory = new CoreCategory(channel.getName(), discordCategory);
        discordCategoryServices.save(discordCategory);
        coreCategoryServices.save(coreCategory);
    }

    public void deleteDiscordCategory(Category channel){
        CoreCategory coreCategory = coreCategoryServices.getByDiscordId(channel.getIdLong());
        coreCategoryServices.delete(coreCategory);
        discordCategoryServices.deleteById(channel.getIdLong());
    }

    public void createCategory(String name){
        DiscordCategory discordCategory = discordCategoryServices.getByName(name);
        CoreCategory coreCategory = new CoreCategory(name, discordCategory);
        coreCategoryServices.save(coreCategory);
    }

    //---Label

    public void createDiscordChannel(TextChannel channel){
        CoreLabel label = coreLabelServices.getOrCreateLabelByName(channel.getName().toLowerCase());
        DiscordCategory discordCategory = discordCategoryServices.getById(channel.getParentCategoryIdLong());

        DiscordChannel discordChannel = new DiscordChannel(
                channel.getIdLong(),
                channel.getName(),
                channel.getType(),
                label,
                discordCategory
        );
        discordChannelServices.save(discordChannel);
    }

    public void deleteDiscordChannel(TextChannel channel){
        DiscordChannel discordChannel = discordChannelServices.getById(channel.getIdLong());
        discordChannelServices.delete(discordChannel);
    }


    public void updateUserTable() {
        discordUserServices.updateTable();
    }
}
