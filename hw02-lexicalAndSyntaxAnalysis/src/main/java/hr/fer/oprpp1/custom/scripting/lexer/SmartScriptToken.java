package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * The {@code SmartScriptToken} class represents type-value pair token instance within a given text.
 */
public class SmartScriptToken {

    /**
     * Type of the current token.
     */
    private final SmartScriptTokenType type;

    /**
     * Value of the current token.
     */
    private final Object value;

    /**
     * Creates a new {@code SmartScriptToken} instance with given type-value pair.
     *
     * @param type one of possible types from {@link SmartScriptTokenType}.
     * @param value string of characters in accordance with the given {@code type}.
     * @throws NullPointerException when {@code type} or {@code value}(when {@code type} is not {@code EOF}) is {@code null}.
     */
    public SmartScriptToken(SmartScriptTokenType type, Object value) {
        if (type == null) throw new NullPointerException("The token type cannot be null");
        if (type != SmartScriptTokenType.EOF && value == null) throw new NullPointerException("The value of the token cannot be null if its type is not EOF");

        this.type = type;
        this.value = value;
    }

    /**
     * Fetches the value of the current token.
     * @return value of the current token.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Fetches the type of the current token.
     * @return type of the current token.
     */
    public SmartScriptTokenType getType() {
        return this.type;
    }
}
