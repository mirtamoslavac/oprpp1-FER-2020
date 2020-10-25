package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * The {@code SmartScriptLexer} class represents a program performing lexical analysis.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class SmartScriptLexer {
    /**
     * Input text for the current lexer.
     */
    private final char[] data;

    /**
     * The last generated token within the current lexer.
     */
    private SmartScriptToken token;

    /**
     * Index of the first nontokenized character.
     */
    private int currentIndex;

    /**
     * The current state of the current lexer.
     */
    private SmartScriptLexerState state;

    /**
     * String of characters that defines the start of the tag.
     */
    private static final String TAG_START = "{$";

    /**
     * String of characters that defines the end of the tag.
     */
    private static final String TAG_END = "$}";

    private boolean checkedTagName;


    /**
     * Creates a new {@code SmartScriptLexer} instance for the given input text.
     *
     * @param text input to be converted to a charArray and tokenized.
     * @throws NullPointerException when the given input text is {@code null}.
     */
    public SmartScriptLexer(String text) {
        if (text == null) throw new NullPointerException("The input text cannot be null");
        this.data = text.toCharArray();
        this.currentIndex = 0;
        this.token = null;
        this.state = SmartScriptLexerState.TEXT;
        this.checkedTagName = false;
    }

    /**
     * Sets the lexer state to the given {@code state}.
     *
     * @param state new lexer state of the current lexer.
     * @throws NullPointerException when the given {@code state} is {@code null}.
     */
    public void setState(SmartScriptLexerState state) {
        if (state == null) throw new NullPointerException("The lexer state cannot be null");
        this.state = state;
    }

    /**
     * Fetches the last generated token.
     *
     * @throws SmartScriptLexerException when trying to fetch the last token within the lexer that hasn't tokenized anything yet.
     * @return last generated token.
     */
    public SmartScriptToken getToken() {
        if (this.token == null) throw new SmartScriptLexerException("Cannot get last token when nothing has been tokenized yet!");
        return this.token;
    }

    /**
     * Generates the next token from the input text, depending on the current {@code state}, and returns it.
     *
     * @throws SmartScriptLexerException when trying to fetch a new token after obtaining EOF as the previous token's type or when unable to generate a token.
     * @return a new {@link SmartScriptToken} instance.
     */
    public SmartScriptToken nextToken() {
        if (this.token != null && this.token.getType() == SmartScriptTokenType.EOF) throw new SmartScriptLexerException("Cannot tokenize after reading EOF!");

        if (this.isEOF(this.currentIndex)) return this.token;
//        this.skipWhitespace();
//        if (this.isEOF(this.currentIndex)) return this.token;

        switch (this.state) {
            case TEXT -> {
                if (this.isTagStart()) return this.tokenizeTagStart();
                else return this.tokenizeText();
            }
            case TAG -> {
                this.skipWhitespace();
                if (!this.checkedTagName) {this.checkedTagName = true; return this.tokenizeTagName();}
                if (this.isTagEnd()) return this.tokenizeTagEnd();
                else return this.tokenizeInsideTag();
            }
            default -> throw new SmartScriptLexerException("Invalid lexer state!");
        }
    }

    private SmartScriptToken tokenizeText() {
        StringBuilder sb = new StringBuilder();

        while (this.currentIndex < this.data.length && !this.isTagStart()) {
            if (this.data[this.currentIndex] == '{') throw new SmartScriptLexerException("Invalid attempt of defining a tag!");
            if (checkEscapingText()) {
                sb.append(this.data[++this.currentIndex]);
                this.currentIndex++;
            } else {
                sb.append(this.data[this.currentIndex++]);
            }
        }

        this.token = new SmartScriptToken(SmartScriptTokenType.STRING_TEXT, sb.toString());
        return this.token;
    }

    private SmartScriptToken tokenizeTagStart() {
        this.currentIndex += 2;

        this.token = new SmartScriptToken(SmartScriptTokenType.TAG_START, TAG_START);
        return this.token;
    }

    private SmartScriptToken tokenizeTagName() {
        if (this.data[this.currentIndex] == '=')
            this.token = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, String.valueOf(this.data[this.currentIndex++]));
        else {
            StringBuilder sb = new StringBuilder();

            while (Character.isLetter(this.data[this.currentIndex]))
                sb.append(this.data[this.currentIndex++]);

            this.token = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, sb.toString().toUpperCase());
        }

        return this.token;
    }

    private SmartScriptToken tokenizeInsideTag() {
        this.skipWhitespace();

        if (Character.isLetter(this.data[this.currentIndex])) return this.tokenizeVariable();

        if (this.data[this.currentIndex] == '@' && this.currentIndex + 1 < this.data.length && Character.isLetter(this.data[this.currentIndex+1])) return this.tokenizeFunction();

        if (this.data[this.currentIndex] == '-' && checkIfNegativeNumber() || Character.isDigit(this.data[this.currentIndex])) return this.tokenizeNumber();

        if (this.checkIfValidOperator(this.data[this.currentIndex])) return this.tokenizeOperator();

        if (this.data[this.currentIndex] == '"') return this.tokenizeString();

        throw new SmartScriptLexerException("Cannot generate a token within the tag!");
    }

    private SmartScriptToken tokenizeTagEnd() {
        this.currentIndex += 2;

        this.checkedTagName = false;

        this.token = new SmartScriptToken(SmartScriptTokenType.TAG_END, TAG_END);
        return this.token;
    }

    private SmartScriptToken tokenizeVariable() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.data[this.currentIndex++]);

        while (this.checkIfMoreValidVariableOrFunctionCharactersFollow())
            sb.append(this.data[this.currentIndex++]);

        this.token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, sb.toString());
        return this.token;
    }

    private SmartScriptToken tokenizeFunction() {
        this.currentIndex++;

        StringBuilder sb = new StringBuilder();

        sb.append(this.data[this.currentIndex++]);

        while (this.checkIfMoreValidVariableOrFunctionCharactersFollow())
            sb.append(this.data[this.currentIndex++]);

        this.token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, sb.toString());
        return this.token;
    }

    private SmartScriptToken tokenizeNumber() {
        int dotCounter = 0;

        StringBuilder sb = new StringBuilder();

        while (Character.isDigit(this.data[this.currentIndex])|| this.data[this.currentIndex] == '.' && dotCounter < 2) {
            if (this.data[this.currentIndex] == '.') dotCounter++;
            sb.append(this.data[this.currentIndex++]);
        }

        try {
            if (dotCounter == 0) {
                this.token = new SmartScriptToken(SmartScriptTokenType.INTEGER, Integer.parseInt(sb.toString()));
            } else {
                this.token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, Double.parseDouble(sb.toString()));
            }
            return this.token;
        } catch (NumberFormatException e) {
            throw new SmartScriptLexerException("The number trying to be tokenized cannot be parsed!");
        }
    }

    private SmartScriptToken tokenizeOperator() {
        this.token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]));
        return this.token;
    }

    private SmartScriptToken tokenizeString() {
        //escape starting "
        this.currentIndex++;

        StringBuilder sb = new StringBuilder();

        while (this.currentIndex < this.data.length && this.data[this.currentIndex] != '"') {
            this.checkWhitespaceChars(sb);
            if (checkEscapingTag()) {
                sb.append(this.data[++this.currentIndex]);
                this.currentIndex++;
            } else {
                sb.append(this.data[this.currentIndex++]);
            }
        }

        //escape ending "
        this.currentIndex++;

        this.token = new SmartScriptToken(SmartScriptTokenType.STRING_TAG, sb.toString());
        return this.token;
    }

    private boolean isTagStart() {
        return this.data[this.currentIndex] == TAG_START.charAt(0) && this.currentIndex + 1 < this.data.length && this.data[this.currentIndex+1] == TAG_START.charAt(1);
    }

    private boolean isTagEnd() {
        return this.data[this.currentIndex] == TAG_END.charAt(0) && this.currentIndex + 1 < this.data.length && this.data[this.currentIndex+1] == TAG_END.charAt(1);
    }

    private void checkWhitespaceChars(StringBuilder sb) {
        if (this.data[this.currentIndex] == '\\' && this.currentIndex + 1 < this.data.length) {
            switch (this.data[this.currentIndex + 1]) {
                case 'n' -> {
                    this.currentIndex += 2;
                    sb.append('\n');
                }
                case 'r' -> {
                    this.currentIndex += 2;
                    sb.append('\r');
                }
                case 't' -> {
                    this.currentIndex += 2;
                    sb.append('\t');
                }
                default -> {}
            }
        }
    }

    private boolean checkEscapingText() {
        if (this.data[this.currentIndex] == '\\') {
            if (!checkEscapedText()) {
                throw new SmartScriptLexerException("Invalid attempt of escaping a character!");
            }
            return true;
        }
        return false;
    }

    private boolean checkEscapedText() {
        return this.currentIndex + 1 < this.data.length && (this.data[this.currentIndex+1] == '\\' || this.data[this.currentIndex+1] == '{');
    }

    private boolean checkEscapingTag() {
        if (this.data[this.currentIndex] == '\\') {
            if (!checkEscapedTag()) {
                throw new SmartScriptLexerException("Invalid attempt of escaping a character!");
            }
            return true;
        }
        return false;
    }

    private boolean checkEscapedTag() {
        return this.currentIndex + 1 < this.data.length && (this.data[this.currentIndex+1] == '\\' || this.data[this.currentIndex+1] == '"');
    }

    private boolean checkIfMoreValidVariableOrFunctionCharactersFollow() {
        return Character.isLetter(this.data[this.currentIndex]) || Character.isDigit(this.data[this.currentIndex]) || this.data[this.currentIndex] == '_';
    }

    private boolean checkIfNegativeNumber() {
        return this.currentIndex + 1 < this.data.length && Character.isDigit(this.data[this.currentIndex+1]);
    }

    private boolean checkIfValidOperator(char character){
        return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
    }

    private boolean isEOF(int index) {
        if (index >= this.data.length) {
            this.token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
            return true;
        }
        return false;
    }

    private void skipWhitespace() {
        for (int i = this.currentIndex, dataLength = this.data.length; i < dataLength; i++, this.currentIndex++) {
            if (this.currentIndex == this.data.length || !this.isWhitespace(this.data[this.currentIndex])) break;
        }
    }

    private boolean isWhitespace(char character) {
        return character == ' ' || character == '\r' || character == '\n' || character == '\t';
    }

}