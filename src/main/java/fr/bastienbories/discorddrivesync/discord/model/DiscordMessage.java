package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DiscordMessage")
@Inheritance(strategy = InheritanceType.JOINED)
public class DiscordMessage {

    @Id
    private long idDiscordMessage;

    private String content;

    @ManyToOne
    @JoinColumn(name = "idDiscordUser")
    private DiscordUser discordUser;

    @ManyToOne
    @JoinColumn(name = "idDiscordChannel")
    private DiscordChannel discordChannel;

    public DiscordMessage(long idDiscordMessage, String content, DiscordUser discordUser, DiscordChannel discordChannel) {
        this.idDiscordMessage = idDiscordMessage;
        this.content = content;
        this.discordUser = discordUser;
        this.discordChannel = discordChannel;
    }

    public DiscordMessage() {

    }
}
