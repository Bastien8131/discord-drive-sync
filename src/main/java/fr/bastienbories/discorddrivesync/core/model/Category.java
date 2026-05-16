package fr.bastienbories.discorddrivesync.core.model;

import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCategory;

    @Column(unique = true)
    private String name;

    @OneToOne
    private DiscordCategory discordCategory;

    @ManyToMany
    @JoinTable(
            name = "POSSESS",
            joinColumns = @JoinColumn(name = "idCategory"),
            inverseJoinColumns = @JoinColumn(name = "idLabel")
    )
    private List<Label> labels;

}
