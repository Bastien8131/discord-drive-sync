package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.common.HasIdAndName;
import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;

import java.util.Objects;

@Entity
@Table(name = "DiscordChannel")
public class DiscordChannel implements HasIdAndName {

    @Id
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLabel")
    private CoreLabel label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordCategory")
    private DiscordCategory discordCategory;

    protected DiscordChannel() {}

    public DiscordChannel(long id, String name, ChannelType type, CoreLabel label, DiscordCategory discordCategory) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.label = label;
        this.discordCategory = discordCategory;
    }

    @Override
    public String toString() {
        return "DiscordChannel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscordChannel that = (DiscordChannel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public CoreLabel getLabel() {
        return label;
    }

    public DiscordCategory getDiscordCategory() {
        return discordCategory;
    }
}
