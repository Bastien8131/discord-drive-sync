package fr.bastienbories.discorddrivesync.common;

public abstract class ResourceIdNotFoundException extends RuntimeException {

    private final long id;

    public ResourceIdNotFoundException(Class<?> entityClass, long id) {
        this(entityClass, "id", id);
    }

    protected ResourceIdNotFoundException(Class<?> entityClass, String idFieldLabel, long id) {
        super(String.format("%s not found for %s: %d", entityClass.getSimpleName(), idFieldLabel, id));
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
