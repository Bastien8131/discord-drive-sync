package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiscordCategory")
public class DiscordCategory {

    @Id
    @Column(name = "id_disc_category")
    private long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "discordCategory")
    private List<DiscordChannel> discordChannels;

    public DiscordCategory() {}

    public DiscordCategory(long idDiscCategory, String name) {
        this.id = idDiscCategory;
        this.name = name;
        this.discordChannels = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DiscordCategory{" +
                "idDiscCategory=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }
}
