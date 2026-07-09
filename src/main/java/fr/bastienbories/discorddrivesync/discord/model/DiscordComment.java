package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordComment")
@PrimaryKeyJoinColumn(name = "idDiscordComment")
@DiscriminatorValue("DISCORD_COMMENT")
public class DiscordComment extends DiscordMessage {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordMessage")
    private DiscordMessage discordReferencedMessage;

    @ManyToOne(fetch = FetchType.LAZY)
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
