package Domain;

public class SyntaxError extends RuntimeException {
    public SyntaxError(String message) {
        super(message);
    }
}