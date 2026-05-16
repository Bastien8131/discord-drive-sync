package fr.bastienbories.discorddrivesync.model.dds;

import fr.bastienbories.discorddrivesync.model.discord.DiscordMessage;
import fr.bastienbories.discorddrivesync.model.drive.DriveFile;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Message")
public class Message extends DiscordMessage {

    @OneToOne
    @JoinColumn(name = "idFile")
    private DriveFile driveFile;

    @ManyToMany
    @JoinTable(
            name = "ASSOCIATE",
            joinColumns = @JoinColumn(name = "idDiscordMessage"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<Label> labels;

}
