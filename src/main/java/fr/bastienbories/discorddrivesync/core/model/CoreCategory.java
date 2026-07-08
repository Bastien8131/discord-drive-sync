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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (idCategory == 0) return false;

        CoreCategory that = (CoreCategory) o;
        return idCategory == that.idCategory;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public long getId() {
        return idCategory;
    }

    public String getName() {
        return name;
    }

    public DiscordCategory getDiscordCategory() {
        return discordCategory;
    }

    public List<CoreLabel> getLabels() {
        return labels;
    }
}
