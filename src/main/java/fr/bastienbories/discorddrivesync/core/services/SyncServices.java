package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordMessageServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SyncServices {

    private final DiscordUserServices discordUserServices;
    private final DiscordCategoryServices discordCategoryServices;
    private final DiscordChannelServices discordChannelServices;
    private final DiscordMessageServices discordMessageServices;

    private final CoreCategoryServices coreCategoryServices;
    private final CoreMessageServices coreMessageServices;
    private final CoreLabelServices coreLabelServices;

    public SyncServices(DiscordUserServices discordUserServices, DiscordCategoryServices discordCategoryServices, DiscordChannelServices discordChannelServices, DiscordMessageServices discordMessageServices, CoreCategoryServices coreCategoryServices, CoreMessageServices coreMessageServices, CoreLabelServices coreLabelServices) {
        this.discordUserServices = discordUserServices;
        this.discordCategoryServices = discordCategoryServices;
        this.discordChannelServices = discordChannelServices;
        this.discordMessageServices = discordMessageServices;
        this.coreCategoryServices = coreCategoryServices;
        this.coreMessageServices = coreMessageServices;
        this.coreLabelServices = coreLabelServices;
    }

    //---Category

    public void createCategoryFromDiscord(Category channel){
        DiscordCategory discordCategory = new DiscordCategory(channel.getIdLong(), channel.getName());
        CoreCategory coreCategory = new CoreCategory(channel.getName(), discordCategory);
        discordCategoryServices.save(discordCategory);
        coreCategoryServices.save(coreCategory);
    }

    public void deleteCategoryFromDiscord(Category channel){
        CoreCategory coreCategory = coreCategoryServices.getByDiscordId(channel.getIdLong());
        coreCategoryServices.delete(coreCategory);
        discordCategoryServices.deleteById(channel.getIdLong());
    }

    //---Channel

    public void createChannelFromDiscord(TextChannel channel){
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

    public void deleteChannelFromDiscord(TextChannel channel){
        DiscordChannel discordChannel = discordChannelServices.getById(channel.getIdLong());
        discordChannelServices.delete(discordChannel);
    }

    //---Message

    public void newMessageFormDiscord(Message message) {
        DiscordUser discordUser = discordUserServices.getByAuthor(message.getAuthor());
        DiscordChannel discordChannel = discordChannelServices.getById(message.getChannelIdLong());
        CoreLabel coreLabel = discordChannel.getLabel();

        CoreMessage coreMessage = new CoreMessage(
                message.getIdLong(),
                message.getContentDisplay(),
                discordUser,
                discordChannel
        );

        coreMessage.addLabel(coreLabel);
        coreMessageServices.save(coreMessage);
    }








    public void updateUserTable() {
        discordUserServices.updateTable();
    }

    public void createCategory(String name){
        DiscordCategory discordCategory = discordCategoryServices.getByName(name);
        CoreCategory coreCategory = new CoreCategory(name, discordCategory);
        coreCategoryServices.save(coreCategory);
    }
}
