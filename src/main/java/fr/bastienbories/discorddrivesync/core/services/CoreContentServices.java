package fr.bastienbories.discorddrivesync.core.services;

import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.repository.CoreContentRepository;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoreContentServices {

    // private static final Logger log = LoggerFactory.getLogger(CoreContentServices.class);

    private final CoreContentRepository coreContentRepository;

    public CoreContentServices(CoreContentRepository coreContentRepository) {
        this.coreContentRepository = coreContentRepository;
    }

    public void save(CoreContent coreContent) {
        coreContentRepository.save(coreContent);
    }

    public boolean dataAlreadyExists(String text) {
        // In the future check other column
        return coreContentRepository.existsByText(text);
    }

    public CoreContent findByText(String text) {
        return coreContentRepository.findByText(text);
    }

    public CoreContent getOrCreate(DiscordUser discordUser, String text){
        text = TextUtils.removeLinkFromContent(text).toString();
        text = TextUtils.removeNewLines(text).toString();
        text = TextUtils.removeChannelTagFromContent(text);

        CoreContent coreContent;

        if (dataAlreadyExists(text)){
            coreContent = findByText(text);
            coreContent.addReposter(discordUser);
        } else {
            coreContent = new CoreContent(text, discordUser);
            save(coreContent);
        }

        return coreContent;
    }

    public CoreContent getOrCreateAndAddDriveFiles(DiscordUser discordUser, String text, List<DriveFile> driveFiles){
        CoreContent coreContent = getOrCreate(discordUser, text);
        coreContent.addDriveFileList(driveFiles);

        return coreContent;
    }

    public List<CoreContent> getAllByCategoryIds(List<Long> ids) {
        return coreContentRepository.findAllByCoreCategoryIds(ids);
    }

    public List<CoreContent> getAllByLabelIds(List<Long> ids) {
        return coreContentRepository.findAllByCoreLabelsIds(ids);
    }

    public List<CoreContent> getAllByLabelIdsAndCategoryIds(List<Long> labelIds, List<Long> categoryIds) {
        return coreContentRepository.findAllByCoreLabelsIdsAndCoreCategoryIds(labelIds, categoryIds);
    }

    public List<CoreContent> findAll() {
        return  coreContentRepository.findAll();
    }

    public Optional<CoreContent> getById(long id) {
        return coreContentRepository.findById(id);
    }

    public List<CoreContent> getAllByAuthorId(long id) {
        return coreContentRepository.findAllByAuthor_Id(id);
    }

    public Optional<CoreContent> getByDiscordMessageId(long id) {
        return coreContentRepository.findByDiscordMessages_Id(id);
    }

    public List<CoreContent> getAllByReposterId(List<Long> ids) {
        return coreContentRepository.findAllByReposterIds(ids);
    }
}
