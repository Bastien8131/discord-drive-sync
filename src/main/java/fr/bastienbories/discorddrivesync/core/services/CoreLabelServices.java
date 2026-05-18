package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.repository.CoreLabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoreLabelServices {

    private final CoreLabelRepository labelRepository;

    public CoreLabelServices(CoreLabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public boolean existByName(String name){
        return labelRepository.findByName(name).isPresent();
    }

    public CoreLabel getLabelByName(String name) {
        return labelRepository.findByName(name)
                .orElseGet(() -> labelRepository.save(new CoreLabel(name)));
    }

}
