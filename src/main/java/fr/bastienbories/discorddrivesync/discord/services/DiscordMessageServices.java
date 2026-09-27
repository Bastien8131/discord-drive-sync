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

    public List<DiscordMessage> getByAuthorId(long id) {
        return discordMessageRepository.findAllByAuthor_Id(id);
    }

    public List<DiscordMessage> getByDiscordChannelId(long id) {
        return discordMessageRepository.findAllByDiscordChannel_Id(id);
    }

    public List<DiscordMessage> getByDiscordCategoryId(long id) {
        return discordMessageRepository.findAllByDiscordChannel_DiscordCategory_Id(id);
    }

    public List<DiscordMessage> getByDiscordCategoryName(String name) {
        return discordMessageRepository.findAllByDiscordChannel_DiscordCategory_Name(name);
    }

    public List<DiscordMessage> getByDiscordCategoryIdOrName(long id, String name) {
        if (id != -1){
            return getByDiscordCategoryId(id);
        }else{
            return getByDiscordCategoryName(name);
        }
    }
}
