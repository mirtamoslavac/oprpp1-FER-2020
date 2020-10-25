package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * The {@code SmartScriptTokenType} represents all possible token types of the {@link SmartScriptToken}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public enum SmartScriptTokenType {
    /**
     * Signals that there are no new characters to be tokenized.
     */
    EOF,

    VARIABLE,
    INTEGER,
    DOUBLE,
    STRING_TEXT,
    STRING_TAG,
    FUNCTION,
    OPERATOR,
    TAG_START,
    TAG_END,
    TAG_NAME
}
