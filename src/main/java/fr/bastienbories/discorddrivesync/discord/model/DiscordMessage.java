package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DiscordMessage")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@DiscriminatorValue("DISCORD_MESSAGE")
public class DiscordMessage {

    @Id
    private long idDiscordMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordMessageData")
    private DiscordMessageData discordMessageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser")
    private DiscordUser discordUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordChannel")
    private DiscordChannel discordChannel;

    protected DiscordMessage() {}

    public DiscordMessage(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel) {
        this.idDiscordMessage = idDiscordMessage;
        this.discordMessageData = discordMessageData;
        this.discordUser = discordUser;
        this.discordChannel = discordChannel;
    }

    @Override
    public String toString() {
        return "DiscordMessage{" +
                "idDiscordMessage=" + idDiscordMessage +
                '}';
    }

    public long getId() {
        return idDiscordMessage;
    }

    public DiscordUser getDiscordUser() {return discordUser;}

    public DiscordMessageData getDiscordMessageData() {
        return discordMessageData;
    }

    public DiscordChannel getDiscordChannel() {
        return discordChannel;
    }
}
