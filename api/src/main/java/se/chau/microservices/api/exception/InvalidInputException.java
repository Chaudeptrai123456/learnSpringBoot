package se.chau.microservices.api.exception;

public class InvalidInputException extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    // Default constructor
    public InvalidInputException() {
        super("Invalid input.");
    }

    // Constructor with a custom error message
    public InvalidInputException(String message) {
        super(message);
    }

    // Constructor with a custom error message and cause
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
