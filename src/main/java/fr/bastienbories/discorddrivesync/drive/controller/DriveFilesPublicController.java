package fr.bastienbories.discorddrivesync.drive.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.bastienbories.discorddrivesync.common.PublicRoute;
import fr.bastienbories.discorddrivesync.drive.services.DriveFileServices;


@Controller
public class DriveFilesPublicController {

    private final DriveFileServices driveFileServices;

    public DriveFilesPublicController(DriveFileServices driveFileServices) {
        this.driveFileServices = driveFileServices;
    }

    @GetMapping(PublicRoute.Paths.DOWNLOAD_FILES + "/{token}")
    public CompletableFuture<ResponseEntity<InputStreamResource>> downloadFileByToken(@PathVariable String token){
        return driveFileServices.findByShareToken(token).map(driveFile -> {
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

    @GetMapping(PublicRoute.Paths.UPLOAD_FILES + "/{token}")
    public String uploadFiles(@PathVariable String token) {
        return "files/upload/upload";
    }
}
