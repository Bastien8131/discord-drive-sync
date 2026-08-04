package fr.bastienbories.discorddrivesync.discord.controller;

import fr.bastienbories.discorddrivesync.discord.exception.DiscordMessageDataNotFoundException;
import fr.bastienbories.discorddrivesync.discord.mapper.DiscordMessageDataMapper;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.dto.DiscordMessageDataDto;
import fr.bastienbories.discorddrivesync.discord.services.DiscordMessageDataServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages/data")
public class DiscordMessageDataController {

    private final DiscordMessageDataServices discordMessageDataServices;
    private final DiscordMessageDataMapper discordMessageDataMapper;

    public DiscordMessageDataController(DiscordMessageDataServices discordMessageDataServices, DiscordMessageDataMapper discordMessageDataMapper) {
        this.discordMessageDataServices = discordMessageDataServices;
        this.discordMessageDataMapper = discordMessageDataMapper;
    }

    @GetMapping("")
    @Transactional(readOnly = true)
    public List<DiscordMessageDataDto> getData(
            @RequestParam(name = "categoryId", required = false) List<Long> categoryIds,
            @RequestParam(name = "labelId", required = false) List<Long> labelIds) {

        List<DiscordMessageData> discordMessageDataList;

        if (categoryIds != null && labelIds != null){
            discordMessageDataList = discordMessageDataServices.getAllByLabelIdsAndCategoryIds(labelIds, categoryIds);
        } else if (categoryIds != null) {
            discordMessageDataList = discordMessageDataServices.getAllByCategoryIds(categoryIds);
        } else if (labelIds != null) {
            discordMessageDataList = discordMessageDataServices.getAllByLabelIds(labelIds);
        } else {
            discordMessageDataList = discordMessageDataServices.findAll();
        }

        return discordMessageDataMapper.discordMessageDataListToDiscordMessageDataDtoList(discordMessageDataList);
    }

}
