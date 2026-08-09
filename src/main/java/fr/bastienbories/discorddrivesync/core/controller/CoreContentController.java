package fr.bastienbories.discorddrivesync.core.controller;

import fr.bastienbories.discorddrivesync.core.exception.CoreContentNotFoundException;
import fr.bastienbories.discorddrivesync.core.mapper.CoreContentMapper;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreContentDto;
import fr.bastienbories.discorddrivesync.core.exception.CoreContentNotFoundByObjectIdException;
import fr.bastienbories.discorddrivesync.core.services.CoreContentServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class CoreContentController {

    private final CoreContentServices coreContentServices;
    private final CoreContentMapper coreContentMapper;

    public CoreContentController(CoreContentServices coreContentServices, CoreContentMapper coreContentMapper) {
        this.coreContentServices = coreContentServices;
        this.coreContentMapper = coreContentMapper;
    }

    @GetMapping(value = "{id}")
    @Transactional(readOnly = true)
    public CoreContentDto getContentById(@PathVariable long id){
        CoreContent coreContent = coreContentServices.getById(id).orElseThrow(() -> new CoreContentNotFoundException(id));
        return coreContentMapper.coreContentToCoreContentDto(coreContent);
    }

    @GetMapping(value = "")
    @Transactional(readOnly = true)
    public List<CoreContentDto> getAllContent(
            @RequestParam(name = "categoryIds", required = false) List<Long> categoryIds,
            @RequestParam(name = "labelIds", required = false) List<Long> labelIds) {

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

    @GetMapping(value = "/author/{id}")
    @Transactional(readOnly = true)
    public List<CoreContentDto> getContentByAuthorId(@PathVariable long id){
        List<CoreContent> coreContents = coreContentServices.getAllByAuthorId(id);
        return coreContentMapper.coreContentListToCoreContentDtoList(coreContents);
    }

    @GetMapping(value = "/reposters")
    @Transactional(readOnly = true)
    public List<CoreContentDto> getContentByReposterId(@RequestParam(name = "ids") List<Long> ids){
        List<CoreContent> coreContents = coreContentServices.getAllByReposterId(ids);
        return coreContentMapper.coreContentListToCoreContentDtoList(coreContents);
    }

    @GetMapping(value = "/message/{id}")
    @Transactional(readOnly = true)
    public CoreContentDto getContentByDiscordMessageId(@PathVariable long id){
        CoreContent coreContent = coreContentServices.getByDiscordMessageId(id).orElseThrow(() -> new CoreContentNotFoundByObjectIdException(DiscordMessage.class, id));
        return coreContentMapper.coreContentToCoreContentDto(coreContent);
    }
}
