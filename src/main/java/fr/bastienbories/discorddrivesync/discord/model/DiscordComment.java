package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordComment")
@PrimaryKeyJoinColumn(name = "idDiscordComment")
public class DiscordComment extends DiscordMessage {

    @ManyToOne
    @JoinColumn(name = "idDiscordMessage")
    private CoreMessage message;

    @ManyToOne
    @JoinColumn(name = "idFile")
    private DriveFile driveFile;
}
