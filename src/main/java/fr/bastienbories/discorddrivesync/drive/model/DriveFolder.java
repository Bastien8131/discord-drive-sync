package fr.bastienbories.discorddrivesync.drive.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "DriveFolder")
public class DriveFolder {

    @Id
    private long idFolder;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String path;
}
