package fr.bastienbories.discorddrivesync.drive.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;

@RestController
@RequestMapping("api/files")
public class DriveFileController {

    private final DriveFileServices driveFileServices;

    public DriveFileController(DriveFileServices driveFileServices) {
        this.driveFileServices = driveFileServices;
    }

    @GetMapping("/{shareToken}")
    public CompletableFuture<ResponseEntity<InputStreamResource>> downloadFileByToken(@PathVariable String shareToken){
        return driveFileServices.findByShareToken(shareToken).map(driveFile -> {
            return driveFileServices.downloadFile(driveFile).thenApply(responseStream -> {
                InputStreamResource resource = new InputStreamResource(responseStream);
                HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + driveFile.getName() + "\"");
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(responseStream.response().contentLength())
                        .contentType(MediaType.parseMediaType(responseStream.response().contentType()))
                        .body(resource);
            });
        }).orElseGet(
            () -> {
                return CompletableFuture.completedFuture(
                        ResponseEntity.notFound().build()
                );
            }
        );
    }
}
