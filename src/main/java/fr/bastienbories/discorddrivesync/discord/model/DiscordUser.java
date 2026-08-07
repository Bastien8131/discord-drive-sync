package fr.bastienbories.discorddrivesync.discord.model;

import fr.bastienbories.discorddrivesync.common.HasIdAndName;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiscordUser")
public class DiscordUser implements HasIdAndName {
    @Id
    private long id;

    private String name;

    private String globalName;

    private String effectiveName;

    private String nickname;

    private String avatarUrl;

    private OffsetDateTime accountCreatedAt;

    private OffsetDateTime joinedAt;

    private int colorRaw;

    @ManyToMany(mappedBy = "reposters")
    private List<CoreContent> repostedContents;

    private boolean isBot;

    private boolean isOwner;

    private boolean isPending;

    protected DiscordUser() {}

    public DiscordUser(Member member){
        User user = member.getUser();
        this.id = member.getIdLong();
        this.name = user.getName();
        this.globalName = user.getGlobalName();
        this.effectiveName = member.getEffectiveName();
        this.nickname = member.getNickname();
        this.avatarUrl = member.getEffectiveAvatarUrl();
        this.accountCreatedAt = user.getTimeCreated();
        this.joinedAt = member.getTimeJoined();
        this.colorRaw = member.getColorRaw();
        this.repostedContents = new ArrayList<>();
        this.isBot = user.isBot();
        this.isOwner = member.isOwner();
        this.isPending = member.isPending();
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGlobalName() {
        return globalName;
    }

    public String getEffectiveName() {
        return effectiveName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public OffsetDateTime getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    public int getColorRaw() {
        return colorRaw;
    }

    public List<CoreContent> getRepostedContents() { return repostedContents; }

    public boolean isBot() {
        return isBot;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public boolean isPending() {
        return isPending;
    }
}
