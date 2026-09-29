package fr.bastienbories.discorddrivesync.common;

public enum PathnameTypeEnum {
    SHARE_FILE_PATH("/api/files"),
    UPLOAD_FILE_PATH("/files/upload");

    public final String pathname;

    PathnameTypeEnum(String pathname) {
        this.pathname = pathname;
    }
}
