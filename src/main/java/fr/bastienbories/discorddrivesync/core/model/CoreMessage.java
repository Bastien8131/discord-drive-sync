package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Message")
@DiscriminatorValue("CORE_MESSAGE")
public class CoreMessage extends DiscordMessage {

    @ManyToMany
    @JoinTable(
            name = "DESCRIPTION",
            joinColumns = @JoinColumn(name = "idDiscordMessage"),
            inverseJoinColumns = @JoinColumn(name = "idFile")
    )
    private List<DriveFile> driveFiles;

    @ManyToMany
    @JoinTable(
            name = "ASSOCIATE",
            joinColumns = @JoinColumn(name = "idDiscordMessage"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> labels;

    protected CoreMessage() {
        super();
    }

    public CoreMessage(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel) {
        super(idDiscordMessage, discordMessageData, discordUser, discordChannel);
        this.driveFiles = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoreMessage that = (CoreMessage) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setDriveFiles(List<DriveFile> driveFiles) {
        this.driveFiles = driveFiles;
    }

    public void addLabel(CoreLabel label) {
        this.labels.add(label);
    }

    public void addDriveFile(DriveFile driveFile) {
        this.driveFiles.add(driveFile);
    }
}
