package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Message")
public class CoreMessage extends DiscordMessage {

    @OneToOne
    @JoinColumn(name = "idFile")
    private DriveFile driveFile;

    @ManyToMany
    @JoinTable(
            name = "ASSOCIATE",
            joinColumns = @JoinColumn(name = "idDiscordMessage"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> labels;

    public CoreMessage(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel) {
        super(idDiscordMessage, discordMessageData, discordUser, discordChannel);
        this.driveFile = null;
        this.labels = new ArrayList<>();
    }

    public CoreMessage(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel, DriveFile driveFile) {
        super(idDiscordMessage, discordMessageData, discordUser, discordChannel);
        this.driveFile = driveFile;
        this.labels = new ArrayList<>();
    }

    public CoreMessage() {
        super();
    }

    @PreRemove
    private void clearLabels() {
        this.labels.clear();
    }

    public void addLabel(CoreLabel label) {
        this.labels.add(label);
    }
}
