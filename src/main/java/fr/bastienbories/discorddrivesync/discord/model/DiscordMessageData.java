package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.core.model.CoreLink;
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

    @ManyToMany(mappedBy = "discordMessageDataList")
    private List<CoreLink> links;

    protected DiscordMessageData() {}

    public DiscordMessageData(String content) {
        this.content = content;
        this.discordMessages = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DiscordMessageData{" +
                "idDiscordMessageData=" + idDiscordMessageData +
                ", content='" + content + '\'' +
                '}';
    }

    public long getIdDiscordMessageData() {
        return idDiscordMessageData;
    }

    public void addLink(CoreLink coreLink) { links.add(coreLink); }
}
