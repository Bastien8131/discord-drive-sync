package fr.bastienbories.discorddrivesync.core.repository;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoreContentRepository extends JpaRepository<CoreContent, Long> {
    boolean existsByText(String text);

    CoreContent findByText(String text);

    @Query("select distinct cc from CoreContent cc " +
            "join cc.labels cl join cl.categories c " +
            "where c.id in :ids")
    List<CoreContent> findAllByCoreCategoryIds(@Param("ids") List<Long> ids);

    @Query("select distinct cc from CoreContent cc " +
            "join cc.labels cl " +
            "where cl.id in :ids")
    List<CoreContent> findAllByCoreLabelsIds(List<Long> ids);

    @Query("select distinct cc from CoreContent cc " +
            "join cc.labels cl join cl.categories c " +
            "where c.id in :categoryIds " +
            "and cl.id in :labelIds")
    List<CoreContent> findAllByCoreLabelsIdsAndCoreCategoryIds(List<Long> labelIds, List<Long> categoryIds);
}
