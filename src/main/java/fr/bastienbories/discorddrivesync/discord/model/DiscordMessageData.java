package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.core.model.CoreLink;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiscordMessageData")
public class DiscordMessageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDiscordMessageData;

    private String content;

    @OneToMany(mappedBy = "discordMessageData")
    private List<DiscordMessage> discordMessages;

    @ManyToMany(mappedBy = "discordMessageDataList")
    private List<CoreLink> coreLinks;

    @ManyToMany
    @JoinTable(
            name = "DESCRIPTION",
            joinColumns = @JoinColumn(name = "idDiscordMessageData"),
            inverseJoinColumns = @JoinColumn(name = "idFile")
    )
    private List<DriveFile> driveFiles;

    protected DiscordMessageData() {}

    public DiscordMessageData(String content) {
        this.content = content;
        this.discordMessages = new ArrayList<>();
        this.coreLinks = new ArrayList<>();
        this.driveFiles = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DiscordMessageData{" +
                "idDiscordMessageData=" + idDiscordMessageData +
                ", content='" + content + '\'' +
                '}';
    }

    public long getId() {
        return idDiscordMessageData;
    }

    public String getContent() {return content;}

    public List<CoreLink> getCoreLinks() {
        return coreLinks;
    }

    public List<DriveFile> getDriveFiles() {
        return driveFiles;
    }

    public void addLink(CoreLink coreLink) { coreLinks.add(coreLink); }

    public void addDriveFile(DriveFile driveFile) { driveFiles.add(driveFile); }
}
