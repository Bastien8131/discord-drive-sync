package fr.bastienbories.discorddrivesync.drive.model;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DriveFile")
public class DriveFile {

    @Id
    private long id;

    private String name;

    private String storageKey;

    private String shareToken;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser", nullable = false)
    private DiscordUser uploader;

    @ManyToMany(mappedBy = "files")
    private List<DiscordMessageData> attachedMessages;

    @ManyToMany
    @JoinTable(
            name = "BIND",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> labels;

    protected DriveFile() {}

    public DriveFile(long id, String name, String storageKey, DiscordUser uploader) {
        this.id = id;
        this.name = name;
        this.storageKey = storageKey;
        this.shareToken = generateShareToken();
        this.uploader = uploader;
        this.attachedMessages = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    private String generateShareToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriveFile driveFile = (DriveFile) o;
        return id == driveFile.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public String getShareToken() {
        return shareToken;
    }

    public void addDiscordMessageData(DiscordMessageData discordMessageData) {
        attachedMessages.add(discordMessageData);
    }
}
