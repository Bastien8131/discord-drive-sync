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
    private long id;

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

    protected CoreCategory() {}

    public CoreCategory(String name, DiscordCategory discordCategory) {
        this.name = name;
        this.discordCategory = discordCategory;
        this.labels = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == 0) return false;

        CoreCategory that = (CoreCategory) o;
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

    public DiscordCategory getDiscordCategory() {
        return discordCategory;
    }

    public List<CoreLabel> getLabels() {
        return labels;
    }

    public void addCoreLabel(CoreLabel coreLabel) {
        labels.add(coreLabel);
    }
}
