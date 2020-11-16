package hr.fer.oprpp1.hw02.prob1;

/**
 * The {@code Lexer} class represents a program performing lexical analysis.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Lexer {
    /**
     * Input text for the current lexer.
     */
    private final char[] data;

    /**
     * The last generated token within the current lexer.
     */
    private Token token;

    /**
     * Index of the first nontokenized character.
     */
    private int currentIndex;

    /**
     * Defined boundary that is used to regulate lexer state.
     */
    private static final char EXTENDED_STATE_BOUNDARY = '#';

    /**
     * The current state of the current lexer.
     */
    private LexerState state;

    /**
     * Creates a new {@code Lexer} instance for the given input text.
     *
     * @param text input to be converted to a charArray and tokenized.
     * @throws NullPointerException when the given input text is {@code null}.
     */
    public Lexer(String text) {
        if (text == null) throw new NullPointerException("The input text cannot be null");

        this.data = text.toCharArray();
        this.currentIndex = 0;
        this.token = null;
        this.state = LexerState.BASIC;
    }

    /**
     * Generates the next token from the input text, depending on the current {@code state}, and returns it.
     *
     * @throws LexerException when trying to fetch a new token after obtaining EOF as the previous token's type or when unable to generate a token.
     * @return a new {@link Token} instance.
     */
    public Token nextToken() {
        if (this.token != null && this.token.getType() == TokenType.EOF) throw new LexerException("Cannot tokenize after reading EOF!");

        if (this.isEOF(this.currentIndex)) return this.token;
        this.skipWhitespace();
        if (this.isEOF(this.currentIndex)) return this.token;

        if (this.state == LexerState.EXTENDED) {
            Token extendedToken = tokenizeStringOfCharactersAsWord();
            if (extendedToken != null) return extendedToken;
        }

        if (Character.isLetter(this.data[this.currentIndex]) || this.data[this.currentIndex] == '\\') return tokenizeWord();

        if (Character.isDigit(this.data[this.currentIndex])) return tokenizeNumber();

        if (!Character.isLetter(this.data[this.currentIndex]) || !Character.isDigit(this.data[this.currentIndex])) return tokenizeSymbol();

        throw new LexerException("Cannot generate a token!");
    }

    /**
     * Fetches the last generated token.
     *
     * @throws LexerException when trying to fetch the last token within the lexer that hasn't tokenized anything yet.
     * @return last generated token.
     */
    public Token getToken() {
        if (this.token == null) throw new LexerException("Cannot get last token when nothing has been tokenized yet!");
        return this.token;
    }

    /**
     * Sets the lexer state to the given {@code state}.
     *
     * @param state new lexer state of the current lexer.
     * @throws NullPointerException when the given {@code state} is {@code null}.
     */
    public void setState(LexerState state) {
        if (state == null) throw new NullPointerException("The state cannot be set to null");
        this.state = state;
    }

    /**
     * Skips all whitespace characters in the {@code data} array until encountering a non-whitespace character;
     */
    private void skipWhitespace() {
        for (int i = this.currentIndex, dataLength = this.data.length; i < dataLength; i++, this.currentIndex++) {
            if (this.currentIndex == this.data.length || !this.isWhitespace(this.data[this.currentIndex])) break;
        }
    }

    /**
     * Determines whether the given {@code character} is a whitespace character.
     *
     * @param character char that is to be checked.
     * @return {@code true} if the given {@code character} is a whitespace character, {@code false} otherwise.
     */
    private boolean isWhitespace(char character) {
        return character == ' ' || character == '\r' || character == '\n' || character == '\t';
    }

    /**
     * Determines whether the given {@code index} is over the upper boundary of the {@code data} array and
     * is a part of the decision whether the tokenization of an EOF token is to be performed.
     *
     * @param index int that is to be checked.
     * @return {@code true} if the given {@code index} is over the upper boundary, {@code false} otherwise.
     */
    private boolean isEOF(int index) {
        if (index >= this.data.length) {
            this.token = new Token(TokenType.EOF, null);
            return true;
        }
        return false;
    }

    /**
     * Creates a new word {@link Token} instance.
     *
     * @throws LexerException when invalid escaping is attempted.
     * @return word {@link Token}.
     */
    private Token tokenizeWord() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.currentIndex, dataLength = this.data.length, escapeBackslashCounter = 0; i < dataLength; i++, this.currentIndex++) {
            if (this.isWhitespace(this.data[i])) {
                this.currentIndex--;
                break;
            }

            if (this.data[i] == '\\' && this.isEOF(this.currentIndex+1)) { //xyz\EOF
                throw new LexerException("Invalid escape!");
            } else if (i-1>=0 && isWhitespace(this.data[i - 1]) && this.data[i] == '\\' && this.isEOF(this.currentIndex)) { //xyz \EOF
                throw new LexerException("Invalid escape!");
            } else if (i-1>=0 && this.data[i] == '\\' && this.data[i-1] == '\\' && ++escapeBackslashCounter % 2 != 0) { //safe from odd number of \
                sb.append(this.data[i]);
                continue;
            } else if (i+1 < this.data.length && this.data[i] == '\\' && (this.isWhitespace(this.data[i+1]) || Character.isLetter(this.data[i+1]))) { // \  or \letter
                throw new LexerException("Invalid escape!");
            } else if (i-1>=0 && !(Character.isLetter(this.data[i]) || this.data[i] == '\\') && this.data[i-1] != '\\') { //a characher that is not a letter or a \ within a word
                                                                                                                          // was not escaped
                this.currentIndex--;
                break;
            } else if (this.data[i] == '\\') { //safe escaping within a word
                continue;
            }

            sb.append(this.data[i]);

        }

        this.currentIndex++;

        return new Token(TokenType.WORD, sb.toString());
    }

    /**
     * Creates a new number {@link Token} instance.
     *
     * @throws LexerException when the generated number cannot be parsed into a long.
     * @return number {@link Token}.
     */
    private Token tokenizeNumber() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.currentIndex, dataLength = this.data.length; i < dataLength; i++, this.currentIndex++) {
            if (this.isWhitespace(this.data[i])) {
                this.currentIndex--;
                break;
            }

            if (!Character.isDigit(this.data[i])) {
                this.currentIndex--;
                break;
            }

            sb.append(this.data[i]);
        }

        this.currentIndex++;

        try {
            this.token =  new Token(TokenType.NUMBER, Long.parseLong(sb.toString()));
            return this.token;
        } catch (NumberFormatException e) {
            throw new LexerException("The number trying to be tokenized cannot be parsed into a long!");
        }
    }

    /**
     * Creates a new symbol {@link Token} instance.
     *
     * @return symbol {@link Token}.
     */
    private Token tokenizeSymbol() {
        this.token = new Token(TokenType.SYMBOL, this.data[currentIndex++]);
        return this.token;
    }

    /**
     * Creates a new word {@link Token} instance within the {@code LexerState.EXTENDED} state.
     *
     * @return word {@link Token}.
     */
    private Token tokenizeStringOfCharactersAsWord() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.currentIndex, dataLength = this.data.length; i < dataLength; i++, this.currentIndex++) {
            if (this.data[i] == EXTENDED_STATE_BOUNDARY) {
                this.state = LexerState.BASIC;
                if (sb.isEmpty()) {
                    return null;
                } else {
                    this.currentIndex--;
                    break;
                }
            }

            if (this.isWhitespace(this.data[i])) {
                this.currentIndex--;
                break;
            }

            sb.append(this.data[i]);

        }

        this.currentIndex++;
        this.token = new Token(TokenType.WORD, sb.toString());

        return this.token;

    }
}
