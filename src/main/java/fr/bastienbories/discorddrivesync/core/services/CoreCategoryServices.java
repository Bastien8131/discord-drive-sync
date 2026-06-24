package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.repository.CoreCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CoreCategoryServices {

    private final CoreCategoryRepository coreCategoryRepository;

    public CoreCategoryServices(CoreCategoryRepository coreCategoryRepository) {
        this.coreCategoryRepository = coreCategoryRepository;
    }

    public Optional<CoreCategory> getById(long id){
        return coreCategoryRepository.findById(id);
    }

    public Optional<CoreCategory> getByIdWithLabels(long id){
        return coreCategoryRepository.findByIdWithLabels(id);
    }

    public Optional<CoreCategory> getByDiscordId(long id){
        return coreCategoryRepository.findCoreCategoryByDiscordCategory_Id(id);
    }

    public void delete(CoreCategory coreCategory) {
        coreCategoryRepository.delete(coreCategory);
    }

    public void save(CoreCategory coreCategory) {
        coreCategoryRepository.save(coreCategory);
    }
}
