package fr.bastienbories.discorddrivesync.common;

public abstract class ResourceNameNotFoundException extends RuntimeException {

    private final String name;

    public ResourceNameNotFoundException(Class<?> entityClass, String name) {
        super(String.format("%s name: %s not found", entityClass.getSimpleName(), name));
        this.name = name;
    }

    public String getName() {return name;}
}
