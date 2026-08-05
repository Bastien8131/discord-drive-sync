package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "DiscordUser")
public class DiscordUser {

    @Id
    private long idDiscordUser;

    private String name;

    @ManyToMany(mappedBy = "reposters")
    private List<DiscordMessageData> discordMessageDataList;

    protected DiscordUser() {}

    public DiscordUser(long idDiscordUser, String name) {
        this.idDiscordUser = idDiscordUser;
        this.name = name;
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
                "idDiscordUser=" + idDiscordUser +
                ", name='" + name + '\'' +
                '}';
    }

    public long getId() {
        return idDiscordUser;
    }

    public String getName() {
        return name;
    }
}
