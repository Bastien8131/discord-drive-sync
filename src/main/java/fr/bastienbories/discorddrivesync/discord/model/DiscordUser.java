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
    private long id;

    private String name;

    @ManyToMany(mappedBy = "reposters")
    private List<DiscordMessageData> repostedMessages;

    protected DiscordUser() {}

    public DiscordUser(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
