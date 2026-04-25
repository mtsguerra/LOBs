package lob.exceptions;

public class LotsOfBallsException extends Exception {
    public LotsOfBallsException(String message) {
        super(message);
    }
    public LotsOfBallsException(String message, Throwable cause) {
        super(message, cause);
    }
}