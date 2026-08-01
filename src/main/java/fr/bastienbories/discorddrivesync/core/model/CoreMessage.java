package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessage;
import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Message")
@DiscriminatorValue("CORE_MESSAGE")
public class CoreMessage extends DiscordMessage {

    @ManyToMany
    @JoinTable(
            name = "ASSOCIATE",
            joinColumns = @JoinColumn(name = "idDiscordMessage"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> coreLabels;

    protected CoreMessage() {
        super();
    }

    public CoreMessage(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel) {
        super(idDiscordMessage, discordMessageData, discordUser, discordChannel);
        this.coreLabels = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoreMessage that = (CoreMessage) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public List<CoreLabel> getCoreLabels() {
        return coreLabels;
    }

    public void addLabel(CoreLabel label) {
        this.coreLabels.add(label);
    }
}
