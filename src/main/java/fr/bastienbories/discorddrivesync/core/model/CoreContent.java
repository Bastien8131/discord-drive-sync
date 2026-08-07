package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CoreContent")
public class CoreContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser")
    private DiscordUser author;

    @ManyToMany
    @JoinTable(
            name = "REPOST",
            joinColumns = @JoinColumn(name = "idCoreContent"),
            inverseJoinColumns = @JoinColumn(name = "idDiscordUser")
    )
    private List<DiscordUser> reposters;

    @OneToMany(mappedBy = "content")
    private List<DiscordMessage> discordMessages;

    @ManyToMany(mappedBy = "referencedInContents")
    private List<CoreLink> links;

    @ManyToMany(mappedBy = "associatedContents")
    private List<CoreLabel> labels;

    @ManyToMany
    @JoinTable(
            name = "DESCRIPTION",
            joinColumns = @JoinColumn(name = "idCoreContent"),
            inverseJoinColumns = @JoinColumn(name = "idFile")
    )
    private List<DriveFile> files;

    protected CoreContent() {}

    public CoreContent(String text, DiscordUser discordUser) {
        this.text = text;
        this.author = discordUser;
        this.reposters = new ArrayList<>();
        this.discordMessages = new ArrayList<>();
        this.links = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CoreContent{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getText() {return text;}

    public DiscordUser getAuthor() { return author; }

    public List<DiscordUser> getReposters() {
        return reposters;
    }

    public List<DiscordMessage> getDiscordMessages() {
        return discordMessages;
    }

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
