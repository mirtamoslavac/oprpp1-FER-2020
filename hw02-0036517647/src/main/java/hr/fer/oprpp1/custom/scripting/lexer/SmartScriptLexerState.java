package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * The {@code SmartScriptLexerState} represents all states the {@link SmartScriptLexer} can find itself in.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public enum SmartScriptLexerState {
    /**
     * Standard lexer state.
     */
    TEXT,

    /**
     * State when within the tag syntax.
     */
    TAG,


}
