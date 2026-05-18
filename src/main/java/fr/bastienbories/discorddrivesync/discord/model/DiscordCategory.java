package fr.bastienbories.discorddrivesync.discord.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiscordCategory")
public class DiscordCategory {

    @Id
    private long idDiscCategory;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "discordCategory")
    private List<DiscordChannel> discordChannels;

    public DiscordCategory() {}

    public DiscordCategory(long idDiscCategory, String name) {
        this.idDiscCategory = idDiscCategory;
        this.name = name;
        this.discordChannels = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DiscordCategory{" +
                "idDiscCategory=" + idDiscCategory +
                ", name='" + name + '\'' +
                ", discordChannels=" + discordChannels +
                '}';
    }

    public void setIdDiscCategory(long idDiscCategory) {
        this.idDiscCategory = idDiscCategory;
    }

    public void setName(String name) {
        this.name = name;
    }
}
