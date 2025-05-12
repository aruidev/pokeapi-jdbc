package Exceptions;

public class DBNotFound extends RuntimeException {
    public DBNotFound(String message) {
        super(message);
    }
}
