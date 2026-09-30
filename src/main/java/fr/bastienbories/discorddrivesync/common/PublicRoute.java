package fr.bastienbories.discorddrivesync.common;

public enum PublicRoute {
    DOWNLOAD_FILE(Paths.DOWNLOAD_FILES),
    UPLOAD_FILE(Paths.UPLOAD_FILES);

    public final String pathname;

    PublicRoute(String pathname) {
        this.pathname = pathname;
    }

    public static final class Paths {
        public static final String DOWNLOAD_FILES = "/files";
        public static final String UPLOAD_FILES = "/files/upload";

        private Paths() {
        }
    }
}
