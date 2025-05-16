package Exceptions;

public class ForeignKeyConstraintException extends RuntimeException {
    public ForeignKeyConstraintException(String message) {
        super(message);
    }
}
