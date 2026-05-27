package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordComment")
@PrimaryKeyJoinColumn(name = "idDiscordComment")
public class DiscordComment extends DiscordMessage {

    @ManyToOne
    @JoinColumn(name = "idDiscordMessage")
    private DiscordMessage discordReferencedMessage;

    @ManyToOne
    @JoinColumn(name = "idFile")
    private DriveFile driveFile;

    public DiscordComment() {

    }

    public DiscordComment(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel, DiscordMessage discordReferencedMessage, DriveFile driveFile) {
        super(idDiscordMessage, discordMessageData, discordUser, discordChannel);
        this.discordReferencedMessage = discordReferencedMessage;
        this.driveFile = driveFile;
    }
}
