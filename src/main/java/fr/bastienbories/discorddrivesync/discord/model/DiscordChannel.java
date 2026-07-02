package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;

import java.util.Objects;

@Entity
@Table(name = "DiscordChannel")
public class DiscordChannel {

    @Id
    private long idDiscordChannel;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLabel")
    private CoreLabel label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscCategory")
    private DiscordCategory discordCategory;

    public DiscordChannel() {}

    public DiscordChannel(long idDiscordChannel, String name, ChannelType type, CoreLabel label, DiscordCategory discordCategory) {
        this.idDiscordChannel = idDiscordChannel;
        this.name = name;
        this.type = type;
        this.label = label;
        this.discordCategory = discordCategory;
    }

    @Override
    public String toString() {
        return "DiscordChannel{" +
                "idDiscordChannel=" + idDiscordChannel +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscordChannel that = (DiscordChannel) o;
        return idDiscordChannel == that.idDiscordChannel;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getName() {
        return name;
    }

    public CoreLabel getLabel() {
        return label;
    }

    public long getId() {
        return idDiscordChannel;
    }
}
