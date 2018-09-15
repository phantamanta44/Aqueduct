package xyz.phanta.aqueduct;

public class ComputationFailedException extends RuntimeException {

    public ComputationFailedException(String message) {
        super(message);
    }

    public ComputationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComputationFailedException(Throwable cause) {
        super(cause);
    }

}
