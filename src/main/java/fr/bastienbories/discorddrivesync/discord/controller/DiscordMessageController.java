package fr.bastienbories.discorddrivesync.discord.controller;

import fr.bastienbories.discorddrivesync.discord.exception.DiscordMessageNotFoundException;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordMessageMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDto;
import fr.bastienbories.discorddrivesync.discord.services.DiscordMessageServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class DiscordMessageController {

    private final DiscordMessageServices discordMessageServices;
    private final DiscordMessageMapper discordMessageMapper;

    public DiscordMessageController(DiscordMessageServices discordMessageServices, DiscordMessageMapper discordMessageMapper) {
        this.discordMessageServices = discordMessageServices;
        this.discordMessageMapper = discordMessageMapper;
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public DiscordMessageDto getMessageById(@PathVariable long id) {
        DiscordMessage discordMessage = discordMessageServices.getById(id).orElseThrow(() -> new DiscordMessageNotFoundException(id));
        return discordMessageMapper.discordMessageToDiscordMessageDto(discordMessage);
    }

    @GetMapping(value = "/author/{id}")
    @Transactional(readOnly = true)
    public List<DiscordMessageDto> getMessagesByAuthorId(@PathVariable long id){
        List<DiscordMessage> discordMessages = discordMessageServices.getByAuthorId(id);
        return discordMessageMapper.discordMessageListToDiscordMessageDtoList(discordMessages);
    }

    @GetMapping(value = "/discordChannel/{id}")
    @Transactional(readOnly = true)
    public List<DiscordMessageDto> getMessagesByDiscordChannelId(@PathVariable long id){
        List<DiscordMessage> discordMessages = discordMessageServices.getByDiscordChannelId(id);
        return discordMessageMapper.discordMessageListToDiscordMessageDtoList(discordMessages);
    }

    @GetMapping(value = "/discordCategory")
    @Transactional(readOnly = true)
    public List<DiscordMessageDto> getMessagesByDiscordCategoryId(
            @RequestParam(name = "id", required = false, defaultValue = "-1") long id,
            @RequestParam(name = "name", required = false) String name
    ){
        List<DiscordMessage> discordMessages = discordMessageServices.getByDiscordCategoryIdOrName(id, name);
        return discordMessageMapper.discordMessageListToDiscordMessageDtoList(discordMessages);
    }
}
