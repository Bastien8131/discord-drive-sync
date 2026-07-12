package fr.bastienbories.discorddrivesync.common;

import org.slf4j.Logger;
import java.util.List;

public final class LogMessages {

    private LogMessages() {}

    //Warning

    public static void notFoundInDatabase(Logger log, Class<?> entityClass, long id) {
        log.warn("{} with id {} not found in database", entityClass.getSimpleName(), id);
    }

    public static void notFoundInTheList(Logger log, Class<?> entityClass, long id, List<?> list) {
        log.warn("{} with id {} was not found in the list (size: {})", entityClass.getSimpleName(), id, list.size());
    }

    //Error

    public static void unexpectedErrorDuringAsyncProcessing(Logger log, Throwable throwable) {
        log.error("An unexpected error occurred during the asynchronous processing: ", throwable);
    }

    public static void unexpectedError(Logger log, Throwable throwable) {
        log.error("An unexpected error occurred: ", throwable);
    }
}
