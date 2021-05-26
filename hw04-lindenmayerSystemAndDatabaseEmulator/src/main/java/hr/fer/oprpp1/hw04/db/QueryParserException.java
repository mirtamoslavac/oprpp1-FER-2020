package hr.fer.oprpp1.hw04.db;

/**
 * Thrown to indicate that something illegal happened while parsing.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class QueryParserException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 202122232425262728L;

    /**
     * Constructs an {@code QueryParserException} with no detail message.
     */
    public QueryParserException() {
        super();
    }

    /**
     * Constructs an {@code QueryParserException} with the specified detail message.
     *
     * @param message the detail message
     */
    public QueryParserException(String message) {
        super(message);
    }

}
