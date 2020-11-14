package hr.fer.oprpp1.hw04.db.lexer;

/**
 * Thrown to indicate that something illegal happened while tokenizing.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class QueryLexerException extends RuntimeException{

    @java.io.Serial
    private static final long serialVersionUID = 202122232425262728L;

    /**
     * Constructs an {@code QueryLexerException} with no detail message.
     */
    public QueryLexerException() {
        super();
    }

    /**
     * Constructs an {@code QueryLexerException} with the specified detail message.
     *
     * @param message the detail message
     */
    public QueryLexerException(String message) {
        super(message);
    }

}