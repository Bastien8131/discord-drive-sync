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
    private long idFile;

    private String name;

    private String path;

    private String shareToken;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser", nullable = false)
    private DiscordUser discordUser;

    @ManyToMany(mappedBy = "driveFiles")
    private List<DiscordMessageData> discordMessageDataList;

    @ManyToMany
    @JoinTable(
            name = "BIND",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> labels;

    protected DriveFile() {}

    public DriveFile(long idFile, String name, String path, DiscordUser discordUser) {
        this.idFile = idFile;
        this.name = name;
        this.path = path;
        this.shareToken = generateShareToken();
        this.discordUser = discordUser;
        this.discordMessageDataList = new ArrayList<>();
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
        return idFile == driveFile.idFile;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public long getId() {
        return idFile;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getShareToken() {
        return shareToken;
    }

    public void addDiscordMessageData(DiscordMessageData discordMessageData) {
        discordMessageDataList.add(discordMessageData);
    }
}
