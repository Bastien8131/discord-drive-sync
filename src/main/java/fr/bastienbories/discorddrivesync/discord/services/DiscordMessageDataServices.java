package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordMessageDataRepository;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiscordMessageDataServices {

    private static final Logger log = LoggerFactory.getLogger(DiscordMessageDataServices.class);

    private final DiscordMessageDataRepository discordMessageDataRepository;

    public DiscordMessageDataServices(DiscordMessageDataRepository discordMessageDataRepository) {
        this.discordMessageDataRepository = discordMessageDataRepository;
    }

    public void save(DiscordMessageData discordMessageData) {
        discordMessageDataRepository.save(discordMessageData);
    }

    public boolean dataAlreadyExists(String content) {
        // In the future check other column
        return discordMessageDataRepository.existsByContent(content);
    }

    public DiscordMessageData findByContent(String contentDisplay) {
        return discordMessageDataRepository.findByContent((contentDisplay));
    }

    public DiscordMessageData getOrCreate(DiscordUser discordUser, String content){
        content = TextUtils.removeLinkFromContent(content).toString();
        content = TextUtils.removeNewLines(content).toString();
        content = TextUtils.removeChannelTagFromContent(content);

        DiscordMessageData discordMessageData;

        if (dataAlreadyExists(content)){
            discordMessageData = findByContent(content);
            discordMessageData.addReposter(discordUser);
        } else {
            discordMessageData = new DiscordMessageData(content, discordUser);
            save(discordMessageData);
        }

        return discordMessageData;
    }

    public DiscordMessageData getOrCreateAndAddDriveFiles(DiscordUser discordUser, String content, List<DriveFile> driveFiles){
        DiscordMessageData discordMessageData = getOrCreate(discordUser, content);
        discordMessageData.addDriveFileList(driveFiles);

        return discordMessageData;
    }

    public List<DiscordMessageData> getAllByCategoryIds(List<Long> ids) {
        return discordMessageDataRepository.findAllByCoreCategoryIds(ids);
    }

    public List<DiscordMessageData> getAllByLabelIds(List<Long> ids) {
        return discordMessageDataRepository.findAllByCoreLabelsIds(ids);
    }

    public List<DiscordMessageData> getAllByLabelIdsAndCategoryIds(List<Long> labelIds, List<Long> categoryIds) {
        return discordMessageDataRepository.findAllByCoreLabelsIdsAndCoreCategoryIds(labelIds, categoryIds);
    }

    public List<DiscordMessageData> findAll() {
        return  discordMessageDataRepository.findAll();
    }
}
