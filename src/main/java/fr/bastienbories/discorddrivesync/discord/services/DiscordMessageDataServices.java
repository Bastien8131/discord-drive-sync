package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.repository.DiscordMessageDataRepository;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.sync.MessageSyncServices;
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

    public DiscordMessageData getOrCreate(String content){
        content = TextUtils.removeLinkFromContent(content).toString();
        content = TextUtils.removeNewLines(content).toString();
        content = TextUtils.removeChannelTagFromContent(content);

        DiscordMessageData discordMessageData;

        if (dataAlreadyExists(content)){
            discordMessageData = findByContent(content);
        } else {
            discordMessageData = new DiscordMessageData(content);
            save(discordMessageData);
        }

        return discordMessageData;
    }

    public DiscordMessageData getOrCreateAndAddDriveFiles(String content, List<DriveFile> driveFiles){
        DiscordMessageData discordMessageData = getOrCreate(content);
        discordMessageData.addDriveFileList(driveFiles);

        return discordMessageData;
    }
}
