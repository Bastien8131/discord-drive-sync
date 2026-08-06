package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordMessageRepository;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiscordMessageServices {

    private final DiscordMessageRepository discordMessageRepository;

    public DiscordMessageServices(DiscordMessageRepository discordMessageRepository) {
        this.discordMessageRepository = discordMessageRepository;
    }


    public void save(DiscordMessage discordMessage) {
        discordMessageRepository.save(discordMessage);
    }

    public boolean checkIsNotValid(Message message) {
        if (message.getPoll() != null){
            return true;
        }
        if (message.getApplicationId() != null){
            return true;
        }
        if (message.getContentDisplay().isEmpty()/* && message.getAttachments().isEmpty()*/){
            return true;
        }

        return false;
    }

    public List<DiscordMessage> getListByData(CoreContent coreContent) {
        return discordMessageRepository.getAllByContent(coreContent);
    }

    public void delete(DiscordMessage discordMessage) {
        discordMessageRepository.delete(discordMessage);
    }

    public Optional<DiscordMessage> getById(long id) {
        return discordMessageRepository.findById(id);
    }

    public boolean dataExistsInSomeChannel(CoreContent coreContent) {
        return discordMessageRepository.existsByContent(coreContent);
    }
}
