package hr.fer.oprpp1.hw02.prob1;

/**
 * The {@code TokenType} enumeration states all allowed token types.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public enum TokenType {
    /**
     * Signals that there are no new characters to be tokenized.
     */
    EOF,
    /**
     * Word, a string of letters or other characters if escaped.
     */
    WORD,
    /**
     * Whole number.
     */
    NUMBER,
    /**
     * Symbol, every character not including alphanumerics or whitespace characters.
     */
    SYMBOL
}
