package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiscordMessageData")
public class DiscordMessageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDiscordMessageData;

    private String content;

    @OneToMany(mappedBy = "discordMessageData")
    private List<DiscordMessage> discordMessages;

    public DiscordMessageData() {}

    public DiscordMessageData(String content) {
        this.content = content;
        this.discordMessages = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }
}
