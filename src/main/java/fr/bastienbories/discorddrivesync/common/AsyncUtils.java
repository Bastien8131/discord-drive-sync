package fr.bastienbories.discorddrivesync.common;

import java.util.concurrent.CompletionException;

public class AsyncUtils {
    public static Throwable unwrap(Throwable ex) {
        if (ex instanceof CompletionException && ex.getCause() != null) {
            return ex.getCause();
        }
        return ex;
    }
}
