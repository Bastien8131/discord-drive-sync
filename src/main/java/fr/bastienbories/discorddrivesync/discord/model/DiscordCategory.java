package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiscordCategory")
public class DiscordCategory {

    @Id
    private long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "discordCategory")
    private List<DiscordChannel> discordChannels;

    protected DiscordCategory() {}

    public DiscordCategory(long id, String name) {
        this.id = id;
        this.name = name;
        this.discordChannels = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DiscordCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }
}
