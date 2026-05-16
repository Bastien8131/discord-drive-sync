package fr.bastienbories.discorddrivesync.model.discord;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "DiscordCategory")
public class DiscordCategory {

    @Id
    private long idDiscCategory;

    private String name;

    @OneToMany(mappedBy = "discordCategory")
    private List<DiscordChannel> discordChannels;
}
