package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.repository.CoreCategoryRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordCategoryRepository;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordChannelRepository;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SyncServices {

    private final DiscordCategoryServices discordCategoryServices;
    private final CoreCategoryServices coreCategoryServices;
    private final CoreLabelServices coreLabelServices;

    public SyncServices(DiscordCategoryServices discordCategoryServices, CoreCategoryServices coreCategoryServices, CoreLabelServices coreLabelServices) {
        this.discordCategoryServices = discordCategoryServices;
        this.coreCategoryServices = coreCategoryServices;
        this.coreLabelServices = coreLabelServices;
    }

    //---

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



//    public void createDiscordChannel(TextChannel channel){
//        CoreLabel label = coreLabelServices.getLabelByName(channel.getName().toLowerCase());
//        DiscordCategory discordCategory = discordCategoryRepository.getReferenceById(channel.getParentCategoryIdLong());
//
//        DiscordChannel discordChannel = new DiscordChannel(
//                channel.getIdLong(),
//                channel.getName(),
//                channel.getType(),
//                label,
//                discordCategory
//        );
//        System.out.println(discordChannel.toString());
//    }
}
