package fr.bastienbories.discorddrivesync.model.discord;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DiscordUser")
public class DiscordUser {

    @Id
    private long idDiscordUser;

    private String name;
}
