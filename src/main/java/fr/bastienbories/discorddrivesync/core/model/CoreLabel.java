package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.common.HasIdAndName;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Label")
public class CoreLabel implements HasIdAndName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "label")
    private List<DiscordChannel> discordChannels;

    @ManyToMany(mappedBy = "labels")
    private List<CoreCategory> categories;

    @ManyToMany(mappedBy = "labels")
    private List<DriveFile> files;

    @ManyToMany
    @JoinTable(
            name = "ASSOCIATE",
            joinColumns = @JoinColumn(name = "idLabel"),
            inverseJoinColumns = @JoinColumn(name = "idCoreContent")
    )
    private List<CoreContent> associatedContents;

    protected CoreLabel() {}

    public CoreLabel(String name) {
        this.name = name;
        this.discordChannels = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.files = new ArrayList<>();
        this.associatedContents = new ArrayList<>();
    }

    public CoreLabel(DiscordChannel discordChannel) {
        this.name = discordChannel.getName();
        this.discordChannels = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.files = new ArrayList<>();
        this.associatedContents = new ArrayList<>();

        this.discordChannels.add(discordChannel);
    }

    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == 0) return false;

        CoreLabel coreLabel = (CoreLabel) o;
        return id == coreLabel.id;
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

    public void addCoreContent(CoreContent coreContent) {
        if (!associatedContents.contains(coreContent)) {
            associatedContents.add(coreContent);
            coreContent.addLabel(this);
        }
    }

    public List<DiscordChannel> getDiscordChannels() {
        return discordChannels;
    }

    
}
