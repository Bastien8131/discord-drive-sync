package fr.bastienbories.discorddrivesync.model.discord;

import fr.bastienbories.discorddrivesync.model.dds.Label;
import jakarta.persistence.*;

@Entity
@Table(name = "DiscordChannel")
public class DiscordChannel {

    @Id
    private long idDiscordChannel;

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscordEnum.Channel type;

    @ManyToOne
    @JoinColumn(name = "idLabel")
    private Label label;

    @ManyToOne
    @JoinColumn(name = "idDiscCategory")
    private DiscordCategory discordCategory;

}
