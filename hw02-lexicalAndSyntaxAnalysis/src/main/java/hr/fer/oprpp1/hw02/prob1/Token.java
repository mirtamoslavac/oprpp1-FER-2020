package hr.fer.oprpp1.hw02.prob1;

/**
 * The {@code Token} class represents type-value pair token instance within a given text.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Token {

    /**
     * Type of the current token.
     */
    private final TokenType type;

    /**
     * Value of the current token.
     */
    private final Object value;

    /**
     * Creates a new {@code Token} instance with given type-value pair.
     *
     * @param type one of possible types from {@link TokenType}.
     * @param value string of characters in accordance with the given {@code type}.
     * @throws NullPointerException when {@code type} or {@code value} is {@code null}.
     */
    public Token(TokenType type, Object value) {
        if (type == null) throw new NullPointerException("The token type cannot be null");
        if (type != TokenType.EOF && value == null) throw new NullPointerException("The value of the token cannot be null if its type is not EOF");

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
    public TokenType getType() {
        return this.type;
    }
}