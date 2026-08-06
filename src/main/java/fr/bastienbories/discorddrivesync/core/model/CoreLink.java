package fr.bastienbories.discorddrivesync.core.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Link")
public class CoreLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String url;

    @ManyToMany
    @JoinTable(
            name = "CONTAIN",
            joinColumns = @JoinColumn(name = "idLink"),
            inverseJoinColumns = @JoinColumn(name = "idCoreContent")
    )
    private List<CoreContent> referencedInContents;

    protected CoreLink() {}

    public CoreLink(String url, CoreContent coreContent) {
        this.url = url;
        this.referencedInContents = new ArrayList<>();
        addCoreContent(coreContent);
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
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void addCoreContent(CoreContent coreContent){
        referencedInContents.add(coreContent);
        coreContent.addLink(this);

    }
}
