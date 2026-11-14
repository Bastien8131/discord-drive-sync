package fr.bastienbories.discorddrivesync.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import net.dv8tion.jda.api.entities.Message.Attachment;

public record FileToUpload(
    String filename,
    String contentType,
    Long discordId,
    CompletableFuture<InputStream> inputStream
) {

    public static FileToUpload fromAttachment(Attachment attachment) {
        return new FileToUpload(
            attachment.getFileName(),
            attachment.getContentType(),
            attachment.getIdLong(),
            attachment.getProxy().download()
        );
    }

    public static FileToUpload fromMultipartFile(MultipartFile mfile){
        CompletableFuture<InputStream> future;

        try {
			future = CompletableFuture.completedFuture(mfile.getInputStream());
		} catch (IOException e) {
			future = CompletableFuture.failedFuture(e);
		}

        return new FileToUpload(
            mfile.getOriginalFilename(), mfile.getContentType(), 
            null, 
            future
        );
    }

    
}
