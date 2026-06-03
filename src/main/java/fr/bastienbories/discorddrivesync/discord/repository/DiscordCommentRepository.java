package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordCommentRepository extends JpaRepository<DiscordComment, Long> {
}
