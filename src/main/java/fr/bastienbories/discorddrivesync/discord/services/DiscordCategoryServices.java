package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DiscordCategoryServices {

    private final DiscordCategoryRepository discordCategoryRepository;

    public DiscordCategoryServices(DiscordCategoryRepository discordCategoryRepository, DiscordApiServices discordApiServices) {
        this.discordCategoryRepository = discordCategoryRepository;
    }

    public boolean existByName(String name) {
        return discordCategoryRepository.findByName(name).isPresent();
    }

    public Optional<DiscordCategory> getById(long id) {
        return discordCategoryRepository.findById(id);
    }

    public void deleteById(long id) {
        discordCategoryRepository.deleteById(id);
    }

    public void save(DiscordCategory discordCategory) {
        discordCategoryRepository.save(discordCategory);
    }
}
