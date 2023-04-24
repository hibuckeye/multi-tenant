package se.callista.blog.management.service;

public class SchemaCreationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SchemaCreationException(String message) {
        super(message);
    }

    public SchemaCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaCreationException(Throwable cause) {
        super(cause);
    }
}
