package fr.bastienbories.discorddrivesync.model.drive;

import fr.bastienbories.discorddrivesync.model.dds.Label;
import fr.bastienbories.discorddrivesync.model.discord.DiscordMessage;
import fr.bastienbories.discorddrivesync.model.discord.DiscordUser;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "DriveFile")
public class DriveFile {

    @Id
    private long idFile;

    private String name;

    private String path;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idDriveFolder", nullable = false)
    private DriveFolder driveFolder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idDiscordUser", nullable = false)
    private DiscordUser discordUser;

    @ManyToOne
    @JoinColumn(name = "idDiscordMessage")
    private DiscordMessage discordMessage;

    @ManyToMany
    @JoinTable(
            name = "BIND",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<Label> labels;

}
