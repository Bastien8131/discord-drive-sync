package fr.bastienbories.discorddrivesync.drive.model;

import fr.bastienbories.discorddrivesync.common.HasIdAndName;
import fr.bastienbories.discorddrivesync.common.PublicRoute;
import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.common.UrlServices;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DriveFile")
public class DriveFile implements HasIdAndName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private Long discordId;

    private String name;

    private String storageKey;

    private String shareToken;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser", nullable = false)
    private DiscordUser uploader;

    @ManyToMany(mappedBy = "files")
    private List<CoreContent> attachedContents;

    @ManyToMany
    @JoinTable(
            name = "BIND",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> labels;

    protected DriveFile() {}

    public DriveFile(String name, String storageKey, DiscordUser uploader) {
        this.discordId = null;
        this.name = name;
        this.storageKey = storageKey;
        this.shareToken = TextUtils.generateToken();
        this.uploader = uploader;
        this.attachedContents = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    public DriveFile(Long discordId, String name, String storageKey, DiscordUser uploader) {
        this.discordId = discordId;
        this.name = name;
        this.storageKey = storageKey;
        this.shareToken = TextUtils.generateToken();
        this.uploader = uploader;
        this.attachedContents = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriveFile driveFile = (DriveFile) o;
        return id != 0 && id == driveFile.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public long getId() {
        return id;
    }

    public Long getDiscordId() {
        return discordId;
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

    public void addCoreContent(CoreContent coreContent) {
        attachedContents.add(coreContent);
    }
}
