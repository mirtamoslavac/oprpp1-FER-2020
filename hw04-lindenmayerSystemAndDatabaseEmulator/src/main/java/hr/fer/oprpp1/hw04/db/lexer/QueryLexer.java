package hr.fer.oprpp1.hw04.db.lexer;

import java.util.Objects;

/**
 * The {@code QueryLexer} class represents an implementation of a lexer for database queries.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class QueryLexer {
    /**
     * Input query text for the current lexer.
     */
    private final char[] data;

    /**
     * The last generated token within the current lexer.
     */
    private QueryToken token;

    /**
     * Index of the first nontokenized character.
     */
    private int currentIndex;

    /**
     * Creates a new {@code QueryLexer} instance for the given input text.
     *
     * @param text input to be converted to a charArray and tokenized.
     * @throws NullPointerException when the given input text is {@code null}.
     */
    public QueryLexer(String text) {
        this.data = Objects.requireNonNull(text, "The input text cannot be null").toCharArray();
        this.currentIndex = 0;
        this.token = null;
    }

    /**
     * Fetches the last generated token.
     *
     * @throws QueryLexerException when trying to fetch the last token within the lexer that hasn't tokenized anything yet.
     * @return last generated token.
     */
    public QueryToken getToken() {
        return Objects.requireNonNull(this.token, "Cannot get last token when nothing has been tokenized yet!");
    }

    /**
     * Generates the next token from the input text, depending on the current {@code state}, and returns it.
     *
     * @throws QueryLexerException when trying to fetch a new token after obtaining EOF as the previous token's type or when unable to generate a token.
     * @return a new {@link QueryToken} instance.
     */
    public QueryToken nextToken() {
        if (this.token != null && this.token.getType() == QueryTokenType.EOF) throw new QueryLexerException("Cannot tokenize after reading EOF!");

        if (this.isEOF(this.currentIndex)) return this.token;

        this.skipWhitespace();
        return this.tokenize();
    }

    /**
     * Returns a newly created {@link QueryToken} instance within a tag.
     *
     * @throws QueryLexerException when unable to recognize a defined token type.
     * @return {@link QueryToken} instance.
     */
    private QueryToken tokenize() {
        if (this.checkIfValidOperator()) return this.tokenizeOperator();

        if (this.data[this.currentIndex] == '"') return this.tokenizeString();

        if (Character.isLetter(this.data[this.currentIndex])) return this.tokenizeFieldValue();

        throw new QueryLexerException("Cannot generate a token within the query because of the '" + this.data[this.currentIndex] + "' character!");
    }

    /**
     * Creates a new field {@link QueryToken} instance.
     *
     * @return field {@link QueryToken}.
     */
    private QueryToken tokenizeFieldValue() {
        StringBuilder sb = new StringBuilder();

        while (checkIfNextCharacterExists(this.currentIndex) && Character.isLetter(this.data[this.currentIndex])) {
            sb.append(this.data[this.currentIndex]);

            ++currentIndex;
        }

        this.token = new QueryToken(QueryTokenType.FIELD_VALUE, sb.toString());
        return this.token;
    }

    /**
     * Creates a new operator {@link QueryToken} instance.
     *
     * @return operator {@link QueryToken}.
     */
    private QueryToken tokenizeOperator() {
        switch (this.data[this.currentIndex]) {
            case '>', '<' -> {
                if (checkIfNextCharacterExists(this.currentIndex + 1) && this.data[this.currentIndex + 1] == '=') {
                    this.token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]) + this.data[this.currentIndex++]);
                } else {
                    this.token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]));
                } return this.token;
            }
            case '=' -> {
                    this.token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]));
                    return this.token;
            }
            case '!' -> {
                if (checkIfNextCharacterExists(this.currentIndex + 1) && this.data[this.currentIndex + 1] == '=') {
                    this.token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]) + this.data[this.currentIndex++]);
                }
                return this.token;
            }
            default -> {
                this.token = new QueryToken(QueryTokenType.OPERATOR, "LIKE");
                this.currentIndex += 4;
                return this.token;
            }
        }
    }

    /**
     * Creates a new string {@link QueryToken} instance.
     *
     * @return string {@link QueryToken}.
     */
    private QueryToken tokenizeString() {
        //ignore starting "
        this.currentIndex++;

        StringBuilder sb = new StringBuilder();

        while (checkIfNextCharacterExists(this.currentIndex) && this.data[this.currentIndex] != '"') {
                sb.append(this.data[this.currentIndex++]);
        }

        //ignore ending "
        this.currentIndex++;

        this.token = new QueryToken(QueryTokenType.STRING, sb.toString());
        return this.token;
    }

    /**
     * Determines whether a valid operator defined by the language follows.
     *
     * @return {@code true} a valid operator follows in the query text, {@code false} otherwise.
     */
    private boolean checkIfValidOperator(){
        return this.data[this.currentIndex] == '>'
                || (this.data[this.currentIndex] == '>' && checkIfNextCharacterExists(this.currentIndex + 1) && this.data[this.currentIndex + 1] == '=')
                || this.data[this.currentIndex] == '<'
                || (this.data[this.currentIndex] == '<' && checkIfNextCharacterExists(this.currentIndex + 1) && this.data[this.currentIndex + 1] == '=')
                || this.data[this.currentIndex] == '='
                || (this.data[this.currentIndex] == '!' && checkIfNextCharacterExists(this.currentIndex + 1) && this.data[this.currentIndex + 1] == '=')
                || (String.valueOf(this.data[this.currentIndex]).equalsIgnoreCase("L") && checkIfNextCharacterExists(this.currentIndex + 1)
                    && String.valueOf(this.data[this.currentIndex + 1]).equalsIgnoreCase("I") && checkIfNextCharacterExists(this.currentIndex + 2)
                    && String.valueOf(this.data[this.currentIndex + 2]).equalsIgnoreCase("K") && checkIfNextCharacterExists(this.currentIndex + 3)
                    && String.valueOf(this.data[this.currentIndex + 3]).equalsIgnoreCase("E") && checkIfNextCharacterExists(this.currentIndex + 4)
                    && isWhitespace(this.data[this.currentIndex + 4]));
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
            this.token = new QueryToken(QueryTokenType.EOF, null);
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

    /**
     * Determines whether the given {@code index} is valid within the current {@code data} array.
     *
     * @param index the index that needs to be checked.
     * @return {@code true} if the position of the index is valid, {@code false} otherwise.
     */
    private boolean checkIfNextCharacterExists(int index) {
        return index < this.data.length;
    }
}
