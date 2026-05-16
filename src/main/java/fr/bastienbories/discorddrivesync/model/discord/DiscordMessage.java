package fr.bastienbories.discorddrivesync.model.discord;

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
}
