package fr.bastienbories.discorddrivesync.common;

public abstract class ResourceIdNotFoundException extends RuntimeException {

    private final long id;
    private final Class<?> entityClass; 

    public ResourceIdNotFoundException(Class<?> entityClass, long id) {
        this(entityClass, "id", id);
    }

    protected ResourceIdNotFoundException(Class<?> entityClass, String idFieldLabel, long id) {
        super(String.format("%s not found for %s: %d", entityClass.getSimpleName(), idFieldLabel, id));
        this.id = id;
        this.entityClass = entityClass;
    }

    public long getId() {
        return id;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
