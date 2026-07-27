package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordMessageData;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Link")
public class CoreLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idLink;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String url;

    @ManyToMany
    @JoinTable(
            name = "CONTAIN",
            joinColumns = @JoinColumn(name = "idLink"),
            inverseJoinColumns = @JoinColumn(name = "idDiscordMessageData")
    )
    private List<DiscordMessageData> discordMessageDataList;

    public CoreLink() {}

    public CoreLink(String url, DiscordMessageData discordMessageData) {
        this.url = url;
        this.discordMessageDataList = new ArrayList<>();
        addDiscordMessageData(discordMessageData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoreLink coreLink = (CoreLink) o;
        return Objects.equals(url, coreLink.url);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return idLink;
    }

    public String getUrl() {
        return url;
    }

    public void addDiscordMessageData(DiscordMessageData discordMessageData){
        discordMessageDataList.add(discordMessageData);
        discordMessageData.addLink(this);

    }
}
