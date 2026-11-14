package fr.bastienbories.discorddrivesync.drive.result;

import fr.bastienbories.discorddrivesync.common.AsyncUtils;
import fr.bastienbories.discorddrivesync.drive.model.DriveFile;

public sealed interface DriveFileResult {
    record Success(DriveFile driveFile) implements DriveFileResult { }
    record Failure(String filename, Throwable throwable) implements DriveFileResult { public Failure {
        throwable = AsyncUtils.unwrap(throwable);
    }}
}
