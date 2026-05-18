package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordCategoryRepository;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiscordCategoryServices {

    private final DiscordCategoryRepository discordCategoryRepository;
    private final DiscordApiServices discordApiServices;

    public DiscordCategoryServices(DiscordCategoryRepository discordCategoryRepository, DiscordApiServices discordApiServices) {
        this.discordCategoryRepository = discordCategoryRepository;
        this.discordApiServices = discordApiServices;
    }

    public boolean existByName(String name) {
        return discordCategoryRepository.findByName(name).isPresent();
    }

    public DiscordCategory getByName(String name) {
        if (existByName(name)){
            return discordCategoryRepository.getDiscordCategoryByName(name);
        } else {
            return create(name);
        }
    }

    public DiscordCategory create(String name){
        Category category = discordApiServices.createCategory(name);
        DiscordCategory discordCategory = new DiscordCategory(category.getIdLong(), category.getName());
        return discordCategoryRepository.save(discordCategory);
    }

    public DiscordCategory getById(long id) {
        return discordCategoryRepository.getReferenceById(id);
    }

    public void deleteById(long idLong) {
        discordCategoryRepository.deleteById(idLong);
    }

    public void save(DiscordCategory discordCategory) {
        discordCategoryRepository.save(discordCategory);
    }
}
