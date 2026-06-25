package fr.bastienbories.discorddrivesync.core.controller;

import fr.bastienbories.discorddrivesync.core.exception.CategoryNotFoundException;
import fr.bastienbories.discorddrivesync.core.mapper.CoreCategoryMapper;
import fr.bastienbories.discorddrivesync.core.model.CoreCategory;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreCategoryDto;
import fr.bastienbories.discorddrivesync.core.services.CoreCategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CoreCategoryController {

    private final CoreCategoryServices coreCategoryServices;
    private final CoreCategoryMapper coreCategoryMapper;

    public CoreCategoryController(CoreCategoryServices coreCategoryServices, CoreCategoryMapper coreCategoryMapper) {
        this.coreCategoryServices = coreCategoryServices;
        this.coreCategoryMapper = coreCategoryMapper;
    }

    @GetMapping("/{id}")
    public CoreCategoryDto getCategoryById(@PathVariable long id){
        CoreCategory coreCategory = coreCategoryServices.getByIdWithLabels(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return coreCategoryMapper.coreCategoryToCoreCategoryDto(coreCategory);
    }
}
