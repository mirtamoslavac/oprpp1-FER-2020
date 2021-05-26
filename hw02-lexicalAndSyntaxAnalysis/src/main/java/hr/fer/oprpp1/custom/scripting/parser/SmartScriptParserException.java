package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Thrown to indicate that something illegal happened while tokenizing.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class SmartScriptParserException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = -147258369963852741L;

    /**
     * Constructs an {@code SmartScriptException} with no detail message.
     */
    public SmartScriptParserException() {
        super();
    }

    /**
     * Constructs an {@code SmartScriptException} with the specified detail message.
     *
     * @param message the detail message
     */
    public SmartScriptParserException(String message) {
        super(message);
    }

}