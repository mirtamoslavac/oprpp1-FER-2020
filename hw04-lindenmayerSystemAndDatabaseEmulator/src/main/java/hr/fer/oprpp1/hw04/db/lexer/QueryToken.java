package hr.fer.oprpp1.hw04.db.lexer;

import java.util.Objects;

/**
 * The {@code QueryToken} class represents type-value pair token instance within a given query.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class QueryToken {
    /**
     * Type of the current token.
     */
    private final QueryTokenType type;

    /**
     * Value of the current token.
     */
    private final String value;

    /**
     * Creates a new {@code QueryToken} instance with given type-value pair.
     *
     * @param type one of possible types from {@link QueryTokenType}.
     * @param value string of characters in accordance with the given {@code type}.
     * @throws NullPointerException when {@code type} or {@code value}(when {@code type} is not {@code EOF}) is {@code null}.
     */
    public QueryToken(QueryTokenType type, String value) {
        this.type = Objects.requireNonNull(type, "The token type cannot be null");

        if (type != QueryTokenType.EOF && value == null) throw new NullPointerException("The value of the token cannot be null if its type is not EOF");
        this.value = value;
    }

    /**
     * Fetches the value of the current token.
     *
     * @return value of the current token.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Fetches the type of the current token.
     *
     * @return type of the current token.
     */
    public QueryTokenType getType() {
        return this.type;
    }
}
