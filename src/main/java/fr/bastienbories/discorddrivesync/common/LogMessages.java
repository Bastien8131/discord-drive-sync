package fr.bastienbories.discorddrivesync.common;

import org.slf4j.Logger;

public final class LogMessages {

    private LogMessages() {}

    public static void notFoundInDatabase(Logger log, Class<?> entityClass, long id) {
        log.warn("{} with id {} not found in database", entityClass.getSimpleName(), id);
    }
}
