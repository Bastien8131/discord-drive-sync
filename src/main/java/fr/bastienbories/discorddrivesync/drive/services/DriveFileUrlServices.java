package fr.bastienbories.discorddrivesync.drive.services;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DriveFileUrlServices {

    private final boolean secure;
    private final String url;
    private final int port;

    public DriveFileUrlServices(
            @Value("${server.secure-http}") boolean secure,
            @Value("${server.url}") String url,
            @Value("${server.port}") int port) {
        this.secure = secure;
        this.url = url;
        this.port = port;
    }

    public String buildUrl(DriveFile driveFile) {
        StringBuilder builder = new StringBuilder();

        if (secure) {
            builder.append("https").append("://").append(url);
        } else {
            builder.append("http").append("://").append(url).append(":").append(port);
        }

        builder.append("/files/").append(driveFile.getShareToken());

        return builder.toString();
    }
}

