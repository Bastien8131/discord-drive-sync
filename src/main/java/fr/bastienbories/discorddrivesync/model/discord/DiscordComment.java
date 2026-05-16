package fr.bastienbories.discorddrivesync.model.discord;

import fr.bastienbories.discorddrivesync.model.dds.Message;
import fr.bastienbories.discorddrivesync.model.drive.DriveFile;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordComment")
@PrimaryKeyJoinColumn(name = "idDiscordComment")
public class DiscordComment extends DiscordMessage {

    @ManyToOne
    @JoinColumn(name = "idDiscordMessage")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "idFile")
    private DriveFile driveFile;
}
