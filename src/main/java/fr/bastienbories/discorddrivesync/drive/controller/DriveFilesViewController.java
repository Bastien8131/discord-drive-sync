package fr.bastienbories.discorddrivesync.drive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller 
@RequestMapping("files")
public class DriveFilesViewController {

    @GetMapping("/upload")
    public String upload() {
        return "files/upload/upload";
    }
}
