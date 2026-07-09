package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.services.CoreCategoryServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategorySyncServices {

    private static final Logger log = LoggerFactory.getLogger(CategorySyncServices.class);

    private final DiscordCategoryServices discordCategoryServices;
    private final CoreCategoryServices coreCategoryServices;

    public CategorySyncServices(DiscordCategoryServices discordCategoryServices, CoreCategoryServices coreCategoryServices) {
        this.discordCategoryServices = discordCategoryServices;
        this.coreCategoryServices = coreCategoryServices;
    }

    public void createCategoryFromDiscord(Category channel){
        DiscordCategory discordCategory = new DiscordCategory(channel.getIdLong(), channel.getName());
        CoreCategory coreCategory = new CoreCategory(channel.getName(), discordCategory);
        discordCategoryServices.save(discordCategory);
        coreCategoryServices.save(coreCategory);
    }

    public void deleteCategoryFromDiscord(Category channel){
        long id = channel.getIdLong();
        coreCategoryServices.getByDiscordId(id).ifPresentOrElse(
            coreCategory -> {
                coreCategoryServices.delete(coreCategory);
                discordCategoryServices.deleteById(id);
            },
            () -> LogMessages.notFoundInDatabase(log, CoreCategory.class, id)
        );
    }
}
