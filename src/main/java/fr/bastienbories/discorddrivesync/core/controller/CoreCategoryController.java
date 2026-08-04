package fr.bastienbories.discorddrivesync.core.controller;

import fr.bastienbories.discorddrivesync.core.exception.CategoryNotFoundException;
import fr.bastienbories.discorddrivesync.core.mapper.CoreCategoryMapper;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreCategoryDto;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreCategoryDtoWithLabelsDto;
import fr.bastienbories.discorddrivesync.core.services.CoreCategoryServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CoreCategoryController {

    private final CoreCategoryServices coreCategoryServices;
    private final CoreCategoryMapper coreCategoryMapper;

    public CoreCategoryController(CoreCategoryServices coreCategoryServices, CoreCategoryMapper coreCategoryMapper) {
        this.coreCategoryServices = coreCategoryServices;
        this.coreCategoryMapper = coreCategoryMapper;
    }

    @GetMapping(value = "/{id}")
    public CoreCategoryDtoWithLabelsDto getCategoryById(@PathVariable long id, @RequestParam(name = "withLabels", defaultValue = "false") boolean withLabels){
        if (withLabels){
            CoreCategory coreCategory = coreCategoryServices.getByIdWithLabels(id).orElseThrow(() -> new CategoryNotFoundException(id));
            return coreCategoryMapper.coreCategoryToCoreCategoryDtoWithLabelsDto(coreCategory);
        }else{
            CoreCategory coreCategory = coreCategoryServices.getById(id).orElseThrow(() -> new CategoryNotFoundException(id));
            return coreCategoryMapper.coreCategoryToCoreCategoryDtoWithoutLabels(coreCategory);
        }
    }

    @GetMapping(value = "")
    public List<CoreCategoryDtoWithLabelsDto> getAllCategories(@RequestParam(name = "withLabels", defaultValue = "false") boolean withLabels){
        if (withLabels){
            List<CoreCategory> coreCategories = coreCategoryServices.getAllWithLabels();
            return coreCategoryMapper.coreCategoryListToCoreCategoryDtoListWithLabelsDto(coreCategories);
        }else {
            List<CoreCategory> coreCategories = coreCategoryServices.getAll();
            return coreCategoryMapper.coreCategoryListToCoreCategoryDtoListWithoutLabels(coreCategories);
        }
    }
}
