package fr.bastienbories.discorddrivesync.core.controller;

import fr.bastienbories.discorddrivesync.core.exception.CoreContentNotFoundException;
import fr.bastienbories.discorddrivesync.core.mapper.CoreContentMapper;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreContentDto;
import fr.bastienbories.discorddrivesync.core.services.CoreContentServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages/content")
public class CoreContentController {

    private final CoreContentServices coreContentServices;
    private final CoreContentMapper coreContentMapper;

    public CoreContentController(CoreContentServices coreContentServices, CoreContentMapper coreContentMapper) {
        this.coreContentServices = coreContentServices;
        this.coreContentMapper = coreContentMapper;
    }

    @GetMapping("{id}")
    public CoreContentDto getDataById(@PathVariable long id){
        CoreContent coreContent = coreContentServices.getById(id).orElseThrow(() -> new CoreContentNotFoundException(id));
        return coreContentMapper.coreContentToCoreContentDto(coreContent);
    }

    @GetMapping("")
    @Transactional(readOnly = true)
    public List<CoreContentDto> getData(
            @RequestParam(name = "categoryId", required = false) List<Long> categoryIds,
            @RequestParam(name = "labelId", required = false) List<Long> labelIds) {

        List<CoreContent> coreContentList;

        if (categoryIds != null && labelIds != null){
            coreContentList = coreContentServices.getAllByLabelIdsAndCategoryIds(labelIds, categoryIds);
        } else if (categoryIds != null) {
            coreContentList = coreContentServices.getAllByCategoryIds(categoryIds);
        } else if (labelIds != null) {
            coreContentList = coreContentServices.getAllByLabelIds(labelIds);
        } else {
            coreContentList = coreContentServices.findAll();
        }

        return coreContentMapper.coreContentListToCoreContentDtoList(coreContentList);
    }

}
