package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * The {@code SmartScriptLexer} class represents a program performing lexical analysis for a defined language.
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

        switch (this.state) {
            case TEXT -> {
                if (this.isTagStart()) return this.tokenizeTagStart();
                else return this.tokenizeText();
            }
            case TAG -> {
                this.skipWhitespace();
                if (this.isTagEnd()) return this.tokenizeTagEnd();
                else return this.tokenizeInsideTag();
            }
            default -> throw new SmartScriptLexerException("Invalid lexer state!");
        }
    }

    /**
     * Returns a newly created text string {@link SmartScriptToken} instance outside a tag.
     *
     * @throws SmartScriptLexerException when attempting to start a tag, but not finishing it.
     * @return string {@link SmartScriptToken}.
     */
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

    /**
     * Creates a new start tag {@link SmartScriptToken} instance within a tag.
     *
     * @return start tag {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeTagStart() {
        this.currentIndex += 2;

        this.token = new SmartScriptToken(SmartScriptTokenType.TAG_START, TAG_START);
        return this.token;
    }

    /**
     * Returns a newly created {@link SmartScriptToken} instance within a tag.
     *
     * @throws SmartScriptLexerException when unable to recognize a defined token type.
     * @return {@link SmartScriptToken} instance.
     */
    private SmartScriptToken tokenizeInsideTag() {
        this.skipWhitespace();

        if (Character.isLetter(this.data[this.currentIndex]) || this.data[this.currentIndex] == '=') return this.tokenizeIdentifier();

        if (this.data[this.currentIndex] == '@' && this.currentIndex + 1 < this.data.length && Character.isLetter(this.data[this.currentIndex+1])) return this.tokenizeFunction();

        if (this.data[this.currentIndex] == '-' && checkIfNegativeNumber() || Character.isDigit(this.data[this.currentIndex])) return this.tokenizeNumber();

        if (this.checkIfValidOperator(this.data[this.currentIndex])) return this.tokenizeOperator();

        if (this.data[this.currentIndex] == '"') return this.tokenizeString();

        throw new SmartScriptLexerException("Cannot generate a token within the tag because of the '" + this.data[this.currentIndex] + "' character!");
    }

    /**
     * Creates a new end tag {@link SmartScriptToken} instance within a tag.
     *
     * @return end tag {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeTagEnd() {
        this.currentIndex += 2;

        this.token = new SmartScriptToken(SmartScriptTokenType.TAG_END, TAG_END);
        return this.token;
    }

    /**
     * Creates a new identifier (tag name or variable) {@link SmartScriptToken} instance within a tag.
     *
     * @return identifier {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeIdentifier() {

        if(this.data[this.currentIndex] == '=') {
            this.token = new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, String.valueOf(this.data[this.currentIndex++]));
            return this.token;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(this.data[this.currentIndex++]);

        while (this.checkIfMoreValidVariableOrFunctionCharactersFollow())
            sb.append(this.data[this.currentIndex++]);

        this.token = new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, sb.toString());
        return this.token;
    }

    /**
     * Creates a new function {@link SmartScriptToken} instance within a tag.
     *
     * @return function {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeFunction() {

        StringBuilder sb = new StringBuilder();

        sb.append(this.data[this.currentIndex++]);
        sb.append(this.data[this.currentIndex++]);

        while (this.checkIfMoreValidVariableOrFunctionCharactersFollow())
            sb.append(this.data[this.currentIndex++]);

        this.token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, sb.toString());
        return this.token;
    }

    /**
     * Creates a new number (integer or double) {@link SmartScriptToken} instance within a tag.
     *
     * @throws NumberFormatException when unable to parse string to integer or double.
     * @return integer or double {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeNumber() {
        int dotCounter = 0;

        StringBuilder sb = new StringBuilder();

        while (Character.isDigit(this.data[this.currentIndex])|| this.data[this.currentIndex] == '.' && dotCounter < 2 || this.data[this.currentIndex] == '-') {
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
            throw new NumberFormatException("The number trying to be tokenized cannot be parsed!");
        }
    }

    /**
     * Creates a new operator {@link SmartScriptToken} instance within a tag.
     *
     * @return operator {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeOperator() {
        this.token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]));
        return this.token;
    }

    /**
     * Creates a new string {@link SmartScriptToken} instance within a tag.
     *
     * @return string {@link SmartScriptToken}.
     */
    private SmartScriptToken tokenizeString() {
        //ignore starting "
        this.currentIndex++;

        StringBuilder sb = new StringBuilder();

        while (this.currentIndex < this.data.length && this.data[this.currentIndex] != '"') {
            this.checkAndAddWhitespaceChars(sb);
            if (checkEscapingTag()) {
                sb.append(this.data[++this.currentIndex]);
                this.currentIndex++;
            } else {
                sb.append(this.data[this.currentIndex++]);
            }
        }

        //ignore ending "
        this.currentIndex++;

        this.token = new SmartScriptToken(SmartScriptTokenType.STRING_TAG, sb.toString());
        return this.token;
    }

    /**
     * Checks whether a tag start is being defined.
     *
     * @return {@code true} if it is being defined, {@code false} otherwise.
     */
    private boolean isTagStart() {
        return this.data[this.currentIndex] == TAG_START.charAt(0) && this.currentIndex + 1 < this.data.length && this.data[this.currentIndex+1] == TAG_START.charAt(1);
    }

    /**
     * Checks whether a tag ending is being defined.
     *
     * @return {@code true} if it is being defined, {@code false} otherwise.
     */
    private boolean isTagEnd() {
        return this.data[this.currentIndex] == TAG_END.charAt(0) && this.currentIndex + 1 < this.data.length && this.data[this.currentIndex+1] == TAG_END.charAt(1);
    }

    /**
     * Looks for and adds, if found, whitespace characters defined through a char sequence within a string in a tag.
     */
    private void checkAndAddWhitespaceChars(StringBuilder sb) {
       whileLoop: while (this.data[this.currentIndex] == '\\' && this.currentIndex + 1 < this.data.length) {
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
               default -> {break whileLoop;}
           }
        }
    }

    /**
     * Determines whether a valid attempt of escaping a character is happening outside the tag.
     *
     * @throws SmartScriptLexerException when trying to escape a invalid character.
     * @return {@code true} if the escaping is valid, {@code false} otherwise.
     */
    private boolean checkEscapingText() {
        if (this.data[this.currentIndex] == '\\') {
            if (!checkEscapedText()) {
                throw new SmartScriptLexerException("Invalid attempt of escaping a character!");
            }
            return true;
        }
        return false;
    }
    /**
     * Determines whether the character attempted to be escaped outside the tag is allowed to be escaped by the language definition.
     *
     * @return {@code true} if the character is allowed to be escaped, {@code false} otherwise.
     */
    private boolean checkEscapedText() {
        return this.currentIndex + 1 < this.data.length && (this.data[this.currentIndex+1] == '\\' || this.data[this.currentIndex+1] == '{');
    }

    /**
     * Determines whether a valid attempt of escaping a character is happening within the tag.
     *
     * @throws SmartScriptLexerException when trying to escape a invalid character.
     * @return {@code true} if the escaping is valid, {@code false} otherwise.
     */
    private boolean checkEscapingTag() {
        if (this.data[this.currentIndex] == '\\') {
            if (!checkEscapedTag()) {
                throw new SmartScriptLexerException("Invalid attempt of escaping a character!");
            }
            return true;
        }
        return false;
    }

    /**
     * Determines whether the character attempted to be escaped within the tag is allowed to be escaped by the language definition.
     *
     * @return {@code true} if the character is allowed to be escaped, {@code false} otherwise.
     */
    private boolean checkEscapedTag() {
        return this.currentIndex + 1 < this.data.length && (this.data[this.currentIndex+1] == '\\' || this.data[this.currentIndex+1] == '\"');
    }

    /**
     * Determines whether the following character is valid in the given context, defined by the language.
     *
     * @return {@code true} if the following character is valid in the given context, {@code false} otherwise.
     */
    private boolean checkIfMoreValidVariableOrFunctionCharactersFollow() {
        return Character.isLetter(this.data[this.currentIndex]) || Character.isDigit(this.data[this.currentIndex]) || this.data[this.currentIndex] == '_';
    }

    /**
     * Determines whether a number follows the current character{@code -} and therefore is a part of the number.
     *
     * @return {@code true} if a number follows, {@code false} otherwise.
     */
    private boolean checkIfNegativeNumber() {
        return this.currentIndex + 1 < this.data.length && Character.isDigit(this.data[this.currentIndex+1]);
    }

    /**
     * Determines whether the given {@code charater} is a valid operator defined by the language.
     *
     * @param character char that is to be checked.
     * @return {@code true} if the given {@code character} is a valid operator, {@code false} otherwise.
     */
    private boolean checkIfValidOperator(char character){
        return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
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
            this.token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
            return true;
        }
        return false;
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

}