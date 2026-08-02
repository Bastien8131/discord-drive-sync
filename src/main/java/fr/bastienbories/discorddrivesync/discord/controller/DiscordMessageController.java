package fr.bastienbories.discorddrivesync.discord.controller;

import fr.bastienbories.discorddrivesync.discord.exception.DiscordMessageNotFoundException;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordMessageMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDto;
import fr.bastienbories.discorddrivesync.discord.services.DiscordMessageServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
