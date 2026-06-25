package fr.bastienbories.discorddrivesync.common;

public abstract class ResourceNotFoundException extends RuntimeException {

    private final long id;

    public ResourceNotFoundException(Class<?> entityClass, long id) {
        super(String.format("%s %d not found", entityClass.getSimpleName(), id));
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
