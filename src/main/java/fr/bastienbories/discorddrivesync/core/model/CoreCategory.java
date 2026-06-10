package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Category")
public class CoreCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCategory;

    @Column(unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private DiscordCategory discordCategory;

    @ManyToMany
    @JoinTable(
            name = "POSSESS",
            joinColumns = @JoinColumn(name = "idCategory"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<CoreLabel> labels;

    public CoreCategory() {}

    public CoreCategory(String name, DiscordCategory discordCategory) {
        this.name = name;
        this.discordCategory = discordCategory;
        this.labels = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", name='" + name + '\'' +
                '}';
    }

    public long getIdCategory() {
        return idCategory;
    }
}
