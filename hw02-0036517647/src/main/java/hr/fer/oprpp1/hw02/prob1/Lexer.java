package hr.fer.oprpp1.hw02.prob1;

/**
 * The {@code Lexer} class represents a
 */
public class Lexer {
    /**
     * Input text for the current lexer.
     */
    private char[] data;

    /**
     * The last generated token within the current lexer.
     */
    private Token token;

    /**
     * Index of the first nontokenized character.
     */
    private int currentIndex;

    /**
     *
     */
    private static final char EXTENDED_STATE_BOUNDARY = '#';

    /**
     *
     */
    private LexerState state;

    /**
     *
     */
    private int boundaryCounter;

    /**
     * Creates a new {@code Lexer} instance for the given input text.
     *
     * @param text input to be converted to a charArray and tokenized.
     * @throws NullPointerException when the given input text is {@code null}.
     */
    public Lexer(String text) {
        if (text == null) throw new NullPointerException("The input text cannot be null");
        this.data = text.toCharArray();
        currentIndex = 0;
        token = null;
        state = LexerState.BASIC;
        boundaryCounter = 0;
    }

    /**
     * Generates the next token from the input text and returns it.
     *
     * @throws LexerException when trying to fetch a new token after obtaining EOF as the previous token's type.
     * @return a new {@link Token} instance.
     */
    public Token nextToken() {
        if (this.token != null && this.token.getType() == TokenType.EOF) throw new LexerException("Cannot tokenize after reading EOF!");

        if (this.isEOF(this.currentIndex)) return this.token;
        skipWhitespace();
        if (this.isEOF(this.currentIndex)) return this.token;

        if (this.state == LexerState.EXTENDED) {
            Token extendedToken = tokenizeStringOfCharactersAsWord();
            if (extendedToken != null) return extendedToken;
        }

        if (this.data[this.currentIndex] == '#' && ++this.boundaryCounter % 2 != 0) this.state = LexerState.EXTENDED;

        if (Character.isLetter(this.data[this.currentIndex]) || this.data[this.currentIndex] == '\\') return tokenizeWord();

        if (Character.isDigit(this.data[this.currentIndex])) return tokenizeNumber();

        if (!Character.isLetter(this.data[this.currentIndex]) || !Character.isDigit(this.data[this.currentIndex])) return tokenizeSymbol();

        return null;
    }

    /**
     * Fetches the last generated token.
     *
     * @return last generated token.
     */
    public Token getToken() {
        return this.token;
    }

    public void setState(LexerState state) {
        if (state == null) throw new NullPointerException("The state cannot be set to null");
        this.state = state;
    }

    private void skipWhitespace() {
        for (int i = this.currentIndex, dataLength = this.data.length; i < dataLength; i++, this.currentIndex++) {
            if (this.currentIndex == this.data.length || !this.isWhitespace(this.data[currentIndex])) {
                break;
            }
        }
    }

    private boolean isWhitespace(char character) {
        return character == ' ' || character == '\r' || character == '\n' || character == '\t';
    }

    private boolean isEOF(int index) {
        if (index >= this.data.length) {
            this.token = new Token(TokenType.EOF, null);
            return true;
        }
        return false;
    }

    private Token tokenizeWord() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.currentIndex, dataLength = this.data.length, escapeBackslashCounter = 0; i < dataLength; i++, this.currentIndex++) {
            if (this.isWhitespace(this.data[i])) {
                this.currentIndex--;
                break;
            }

            if (this.data[i] == '\\' && this.isEOF(this.currentIndex+1)) {
                throw new LexerException("Invalid escape!");
            } else if (i-1>=0 && isWhitespace(this.data[i - 1]) && this.data[i] == '\\' && this.isEOF(this.currentIndex)) {
                throw new LexerException("Invalid escape!");
            } else if (i-1>=0 && this.data[i] == '\\' && this.data[i-1] == '\\' && ++escapeBackslashCounter % 2 != 0) {
                sb.append(this.data[i]);
                continue;
            } else if (i+1 < this.data.length && this.data[i] == '\\' && (this.isWhitespace(this.data[i+1]) || Character.isLetter(this.data[i+1]))) {
                throw new LexerException("Invalid escape!");
            } else if (i-1>=0 && !(Character.isLetter(this.data[i]) || this.data[i] == '\\') && this.data[i-1] != '\\') {
                this.currentIndex--;
                break;
            } else if (this.data[i] == '\\') {
                continue;
            }

            sb.append(this.data[i]);

        }

        this.currentIndex++;

        return new Token(TokenType.WORD, sb.toString());
    }

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
            throw new LexerException("The number trying to be tokenized is too big for a long!");
        }


    }

    private Token tokenizeSymbol() {
        this.token = new Token(TokenType.SYMBOL, this.data[currentIndex++]);
        return this.token;
    }

    private Token tokenizeStringOfCharactersAsWord() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.currentIndex, dataLength = this.data.length; i < dataLength; i++, this.currentIndex++) {
            if (this.data[i] == '#') {
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
