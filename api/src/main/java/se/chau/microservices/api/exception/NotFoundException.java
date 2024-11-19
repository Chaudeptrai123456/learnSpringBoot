package se.chau.microservices.api.exception;

public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    // Default constructor with a generic message
    public NotFoundException() {
        super("Resource not found.");
    }

    // Constructor with a custom error message
    public NotFoundException(String message) {
        super(message);
    }

    // Constructor with a custom error message and a cause
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with just a cause
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
