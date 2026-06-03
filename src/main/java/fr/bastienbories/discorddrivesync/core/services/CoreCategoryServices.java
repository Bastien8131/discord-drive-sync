package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.repository.CoreCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoreCategoryServices {

    private final CoreCategoryRepository coreCategoryRepository;

    public CoreCategoryServices(CoreCategoryRepository coreCategoryRepository) {
        this.coreCategoryRepository = coreCategoryRepository;
    }

    public CoreCategory getByDiscordId(long id){
        return coreCategoryRepository.getCoreCategoryByDiscordCategory_IdDiscCategory(id);
    }

    public void delete(CoreCategory coreCategory) {
        coreCategoryRepository.delete(coreCategory);
    }

    public void save(CoreCategory coreCategory) {
        coreCategoryRepository.save(coreCategory);
    }
}
