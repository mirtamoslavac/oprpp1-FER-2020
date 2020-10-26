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

    /**
     * Variable or tag name within a tag.
     */
    IDENTIFIER,

    /**
     * Constant integer value within a tag.
     */
    INTEGER,

    /**
     * Constant double value within a tag.
     */
    DOUBLE,

    /**
     * Textual data outside any tag.
     */
    STRING_TEXT,

    /**
     * Textual data surrounded with double quotation marks within a tag.
     */
    STRING_TAG,

    /**
     * Function name within a tag.
     */
    FUNCTION,

    /**
     * Operator within a tag.
     */
    OPERATOR,

    /**
     * Start of a tag.
     */
    TAG_START,

    /**
     * End of a tag.
     */
    TAG_END,
}
