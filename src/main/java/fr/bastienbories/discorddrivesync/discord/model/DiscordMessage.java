package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordMessage")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@DiscriminatorValue("DISCORD_MESSAGE")
public class DiscordMessage {

    @Id
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCoreContent")
    private CoreContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser")
    private DiscordUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordChannel")
    private DiscordChannel discordChannel;

    protected DiscordMessage() {}

    public DiscordMessage(long id, CoreContent content, DiscordUser author, DiscordChannel discordChannel) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.discordChannel = discordChannel;
    }

    @Override
    public String toString() {
        return "DiscordMessage{" +
                "id=" + id +
                '}';
    }

    public long getId() {
        return id;
    }

    public DiscordUser getAuthor() {return author;}

    public CoreContent getContent() {
        return content;
    }

    public DiscordChannel getDiscordChannel() {
        return discordChannel;
    }
}
