package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordComment")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("DISCORD_COMMENT")
public class DiscordComment extends DiscordMessage {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordReferencedMessage")
    private DiscordMessage discordReferencedMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFile")
    private DriveFile file;

    protected DiscordComment() {}

    public DiscordComment(long id, DiscordMessageData discordMessageData, DiscordUser author, DiscordChannel discordChannel, DiscordMessage discordReferencedMessage, DriveFile file) {
        super(id, discordMessageData, author, discordChannel);
        this.discordReferencedMessage = discordReferencedMessage;
        this.file = file;
    }
}
