package hr.fer.oprpp1.hw04.db.lexer;

/**
 * The {@code QueryTokenType} represents all possible token types of the {@link QueryToken}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public enum QueryTokenType {
    /**
     * Signals that there are no new characters to be tokenized.
     */
    EOF,

    /**
     *
     */
    FIELD_VALUE,

    /**
     *
     */
    STRING,

    /**
     * Operator within a query.
     */
    OPERATOR,
}
