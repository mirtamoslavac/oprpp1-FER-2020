package hr.fer.oprpp1.hw02.prob1;

public enum LexerState {
    /**
     * Signals that there are no new characters to be tokenized.
     */
    BASIC,
    /**
     * Word, a string of letters or other characters if escaped.
     */
    EXTENDED,

}
