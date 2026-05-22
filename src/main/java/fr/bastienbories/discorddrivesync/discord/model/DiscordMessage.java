package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DiscordMessage")
@Inheritance(strategy = InheritanceType.JOINED)
public class DiscordMessage {

    @Id
    private long idDiscordMessage;

    @ManyToOne
    @JoinColumn(name = "idDiscordMessageData")
    private DiscordMessageData discordMessageData;

    @ManyToOne
    @JoinColumn(name = "idDiscordUser")
    private DiscordUser discordUser;

    @ManyToOne
    @JoinColumn(name = "idDiscordChannel")
    private DiscordChannel discordChannel;

    public DiscordMessage(long idDiscordMessage, DiscordMessageData discordMessageData, DiscordUser discordUser, DiscordChannel discordChannel) {
        this.idDiscordMessage = idDiscordMessage;
        this.discordMessageData = discordMessageData;
        this.discordUser = discordUser;
        this.discordChannel = discordChannel;
    }

    public DiscordMessage() {

    }

    public DiscordMessageData getDiscordMessageData() {
        return discordMessageData;
    }
}
