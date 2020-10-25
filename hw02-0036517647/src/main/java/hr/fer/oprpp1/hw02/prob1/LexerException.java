package hr.fer.oprpp1.hw02.prob1;

/**
 * Thrown to indicate that something illegal happened while tokenizing.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class LexerException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = -1234567890987654321L;

    /**
     * Constructs an {@code LexerException} with no detail message.
     */
    public LexerException() {
    }

    /**
     * Constructs an {@code LexerException} with the specified detail message.
     *
     * @param message the detail message
     */
    public LexerException(String message) {
        super(message);
    }

}
