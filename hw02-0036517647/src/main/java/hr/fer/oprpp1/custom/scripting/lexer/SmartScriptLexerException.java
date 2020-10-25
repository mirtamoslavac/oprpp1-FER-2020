package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Thrown to indicate that something illegal happened while tokenizing.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class SmartScriptLexerException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = 753869421124357689L;

    /**
     * Constructs an {@code SmartScriptException} with no detail message.
     */
    public SmartScriptLexerException() {
        super();
    }

    /**
     * Constructs an {@code SmartScriptException} with the specified detail message.
     *
     * @param message the detail message
     */
    public SmartScriptLexerException(String message) {
        super(message);
    }

}
