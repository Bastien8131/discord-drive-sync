package fr.bastienbories.discorddrivesync.discord.controller;

import fr.bastienbories.discorddrivesync.discord.exception.DiscordUserNotFoundException;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordUserMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordUserDto;
import fr.bastienbories.discorddrivesync.discord.services.DiscordUserServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class DiscordUserController {

    private final DiscordUserServices discordUserServices;
    private final DiscordUserMapper discordUserMapper;

    public DiscordUserController(DiscordUserServices discordUserServices, DiscordUserMapper discordUserMapper) {
        this.discordUserServices = discordUserServices;
        this.discordUserMapper = discordUserMapper;
    }

    @GetMapping("{id}")
    public DiscordUserDto getUser(@PathVariable long id){
        DiscordUser discordUser = discordUserServices.getById(id).orElseThrow(() -> new DiscordUserNotFoundException(id));
        return discordUserMapper.discordUserToDiscordUserDto(discordUser);
    }
}
