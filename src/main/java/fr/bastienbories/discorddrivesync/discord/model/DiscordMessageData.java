package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
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
    private long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser")
    private DiscordUser author;

    @ManyToMany
    @JoinTable(
            name = "REPOST",
            joinColumns = @JoinColumn(name = "idDiscordMessageData"),
            inverseJoinColumns = @JoinColumn(name = "idDiscordUser")
    )
    private List<DiscordUser> reposters;

    @OneToMany(mappedBy = "discordMessageData")
    private List<DiscordMessage> discordMessages;

    @ManyToMany(mappedBy = "referencedInMessages")
    private List<CoreLink> links;

    @ManyToMany(mappedBy = "associatedMessages")
    private List<CoreLabel> labels;

    @ManyToMany
    @JoinTable(
            name = "DESCRIPTION",
            joinColumns = @JoinColumn(name = "idDiscordMessageData"),
            inverseJoinColumns = @JoinColumn(name = "idFile")
    )
    private List<DriveFile> files;

    protected DiscordMessageData() {}

    public DiscordMessageData(String content, DiscordUser discordUser) {
        this.content = content;
        this.author = discordUser;
        this.reposters = new ArrayList<>();
        this.discordMessages = new ArrayList<>();
        this.links = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DiscordMessageData{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getContent() {return content;}

    public DiscordUser getAuthor() { return author; }

    public List<CoreLink> getLinks() {
        return links;
    }

    public List<CoreLabel> getLabels() {
        return labels;
    }

    public List<DriveFile> getFiles() {
        return files;
    }

    public void addLink(CoreLink coreLink) { links.add(coreLink); }

    public void addLabel(CoreLabel coreLabel) { labels.add(coreLabel); }

    public void addDriveFile(DriveFile driveFile) { files.add(driveFile); }

    public void addDriveFileList(List<DriveFile> driveFiles){
        for (DriveFile driveFile : driveFiles) {
            if (!getFiles().contains(driveFile)){
                addDriveFile(driveFile);
            }
        }
    }
    public void addReposter(DiscordUser discordUser){
        if (!reposters.contains(discordUser)){
            reposters.add(discordUser);
        }
    }
}
