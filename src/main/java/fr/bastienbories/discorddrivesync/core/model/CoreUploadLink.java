package fr.bastienbories.discorddrivesync.core.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import fr.bastienbories.discorddrivesync.common.HasId;
import fr.bastienbories.discorddrivesync.common.TextUtils;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.model.DiscordUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UploadLink")
public class CoreUploadLink implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(columnDefinition = "TEXT")
    private String interactionToken;

    @Column(nullable = false)
    private boolean used = false;

    private OffsetDateTime createdAt;

    private OffsetDateTime endedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordUser", nullable = false)
    private DiscordUser user; //summoner and consumer

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiscordChannel", nullable = false)
    private DiscordChannel discordChannel;

    protected CoreUploadLink() {}

    public CoreUploadLink(DiscordUser user, DiscordChannel discordChannel, String interactionToken) {
        this.token = TextUtils.generateToken();
        this.interactionToken = interactionToken; //token from event slash command /upload
        this.used = false;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.endedAt = this.createdAt.plusHours(1);
        this.user = user;
        this.discordChannel = discordChannel;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getInteractionToken() {
        return interactionToken;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getEndedAt() {
        return endedAt;
    }

    public DiscordUser getUser() {
        return user;
    }

    public DiscordChannel getDiscordChannel() {
        return discordChannel;
    }

    public boolean isExpired(OffsetDateTime endedAt){
        return createdAt.isBefore(endedAt);
    }

    public boolean isExpired(){
        return isExpired(endedAt);
    }

    public boolean isInteractionExpired(){
        return isExpired(createdAt.plusMinutes(15));
    }
    
}
