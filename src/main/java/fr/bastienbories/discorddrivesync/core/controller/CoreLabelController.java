package fr.bastienbories.discorddrivesync.core.controller;

import fr.bastienbories.discorddrivesync.core.exception.CoreLabelIdNotFoundException;
import fr.bastienbories.discorddrivesync.core.exception.CoreLabelNameNotFoundException;
import fr.bastienbories.discorddrivesync.core.mapper.CoreLabelMapper;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreLabelDto;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class CoreLabelController {

    private final CoreLabelServices coreLabelServices;
    private final CoreLabelMapper coreLabelMapper;

    public CoreLabelController(CoreLabelServices coreLabelServices, CoreLabelMapper coreLabelMapper) {
        this.coreLabelServices = coreLabelServices;
        this.coreLabelMapper = coreLabelMapper;
    }

    @GetMapping(value = "")
    public List<CoreLabelDto> getAllLabels(){
        List<CoreLabel> coreLabels = coreLabelServices.getAll();
        return coreLabelMapper.coreLabelListToCoreLabelDtoList(coreLabels);
    }

    @GetMapping(value = "{id}")
    public CoreLabelDto getLabelsById(@PathVariable long id){
        CoreLabel coreLabel = coreLabelServices.getById(id).orElseThrow(() -> new CoreLabelIdNotFoundException(id));
        return coreLabelMapper.coreLabelToCoreLabelDto(coreLabel);
    }

    @GetMapping(value = "name/{name}")
    public CoreLabelDto getLabelsById(@PathVariable String name){
        CoreLabel coreLabel = coreLabelServices.getByName(name).orElseThrow(() -> new CoreLabelNameNotFoundException(name));
        return coreLabelMapper.coreLabelToCoreLabelDto(coreLabel);
    }
}
