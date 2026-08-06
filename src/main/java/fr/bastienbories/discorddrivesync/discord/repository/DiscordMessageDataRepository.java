package fr.bastienbories.discorddrivesync.discord.repository;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscordMessageDataRepository extends JpaRepository<DiscordMessageData, Long> {
    boolean existsByContent(String contentDisplay);

    DiscordMessageData findByContent(String content);

    @Query("select distinct dmd from DiscordMessageData dmd " +
            "join dmd.labels cl join cl.categories c " +
            "where c.id in :ids")
    List<DiscordMessageData> findAllByCoreCategoryIds(@Param("ids") List<Long> ids);

    @Query("select distinct dmd from DiscordMessageData dmd " +
            "join dmd.labels cl " +
            "where cl.id in :ids")
    List<DiscordMessageData> findAllByCoreLabelsIds(List<Long> ids);

    @Query("select distinct dmd from DiscordMessageData dmd " +
            "join dmd.labels cl join cl.categories c " +
            "where c.id in :categoryIds " +
            "and cl.id in :labelIds")
    List<DiscordMessageData> findAllByCoreLabelsIdsAndCoreCategoryIds(List<Long> labelIds, List<Long> categoryIds);
}
