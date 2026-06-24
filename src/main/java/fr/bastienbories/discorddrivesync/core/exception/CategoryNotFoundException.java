package fr.bastienbories.discorddrivesync.core.exception;

public class CategoryNotFoundException extends RuntimeException {

    private final long id;

    public CategoryNotFoundException(long id) {
        super("Category " + id + " not found");
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
