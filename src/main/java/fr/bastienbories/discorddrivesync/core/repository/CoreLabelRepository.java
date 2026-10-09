package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CoreLabelRepository extends JpaRepository<CoreLabel, Long> {
    Optional<CoreLabel> findByName(String name);

    @Query (
        "select distinct cl.id from CoreLabel cl " +
        "join cl.discordChannels dc " +
        "where dc.id in :ids"
    )
    List<Long> findIdsByDiscordChannelIds(List<Long> ids);
}
