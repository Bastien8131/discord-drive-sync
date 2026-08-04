package fr.bastienbories.discorddrivesync.common;

public abstract class ResourceIdNotFoundException extends RuntimeException {

    private final long id;

    public ResourceIdNotFoundException(Class<?> entityClass, long id) {
        super(String.format("%s id: %d not found", entityClass.getSimpleName(), id));
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
