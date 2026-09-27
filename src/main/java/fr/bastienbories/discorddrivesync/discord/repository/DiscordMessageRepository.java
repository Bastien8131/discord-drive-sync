package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscordMessageRepository extends JpaRepository<DiscordMessage, Long> {
    List<DiscordMessage> getAllByContent(CoreContent coreContent);

    boolean existsByContent(CoreContent coreContent);

    List<DiscordMessage> findAllByAuthor_Id(long authorId);

    List<DiscordMessage> findAllByDiscordChannel_Id(long discordChannelId);

    List<DiscordMessage> findAllByDiscordChannel_DiscordCategory_Id(long discordChannelDiscordCategoryId);

    List<DiscordMessage> findAllByDiscordChannel_DiscordCategory_Name(String discordChannelDiscordCategoryName);

}
