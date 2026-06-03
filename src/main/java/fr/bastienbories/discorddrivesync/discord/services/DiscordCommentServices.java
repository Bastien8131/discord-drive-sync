package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.discord.model.DiscordComment;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiscordCommentServices {

    private final DiscordCommentRepository discordCommentRepository;

    public DiscordCommentServices(DiscordCommentRepository discordCommentRepository) {
        this.discordCommentRepository = discordCommentRepository;
    }

    public void save(DiscordComment discordComment) {
        discordCommentRepository.save(discordComment);
    }
}
