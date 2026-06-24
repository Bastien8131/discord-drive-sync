package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Label")
public class CoreLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idLabel;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "label")
    private List<DiscordChannel> discordChannels;

    @ManyToMany(mappedBy = "labels")
    private List<CoreCategory> categories;

    @ManyToMany(mappedBy = "labels")
    private List<DriveFile> driveFiles;

    @ManyToMany(mappedBy = "labels")
    private List<CoreMessage> messages;

    public CoreLabel() {}

    public CoreLabel(String name) {
        this.name = name;
        this.discordChannels = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.driveFiles = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public CoreLabel(DiscordChannel discordChannel) {
        this.name = discordChannel.getName();
        this.discordChannels = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.driveFiles = new ArrayList<>();
        this.messages = new ArrayList<>();

        this.discordChannels.add(discordChannel);
    }

    @Override
    public String toString() {
        return "Label{" +
                "idLabel=" + idLabel +
                ", name='" + name + '\'' +
                '}';
    }

    public void addDiscordChannel(DiscordChannel discordChannel){
        this.discordChannels.add(discordChannel);
    }

    public long getId() {
        return idLabel;
    }

    public String getName() {
        return name;
    }
}
