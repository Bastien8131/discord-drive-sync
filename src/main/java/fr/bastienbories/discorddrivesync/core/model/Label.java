package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Label")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idLabel;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "label")
    private List<DiscordChannel> discordChannels;

    @ManyToMany(mappedBy = "labels")
    private List<Category> categories;

    @ManyToMany(mappedBy = "labels")
    private List<DriveFile> driveFiles;

    @ManyToMany(mappedBy = "labels")
    private List<Message> messages;
}
