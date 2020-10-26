package hr.fer.oprpp1.custom.scripting.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmartScriptLexerTest {

    @Test
    public void testNotNull() {
        assertNotNull(new SmartScriptLexer("").nextToken());
    }

    @Test
    public void testNullInput() {
        assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
    }

    @Test
    public void testEmpty() {
       checkEOF(new SmartScriptLexer(""));
    }

    @Test
    public void testSetStateNull() {
        assertThrows(NullPointerException.class, () -> new SmartScriptLexer("").setState(null));
    }

    @Test
    public void testGetReturnsLastNext() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(token, lexer.getToken());
        assertEquals(token, lexer.getToken());
    }

    @Test
    public void testTokenizingAfterEOF() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        checkEOF(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testStringText() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is a test");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.STRING_TEXT, token.getType());
        assertEquals("This is a test", token.getValue());

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testStringTextInvalidOpenCurlyBracketThrows() {
        assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("This is a te{st").nextToken());
    }

    @Test
    public void testStringTextValidBackslashEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("This \\\\is a test");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.STRING_TEXT, token.getType());
        assertEquals("This \\is a test", token.getValue());

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testStringTextValidOpenCurlyBracketEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("This \\{is a test");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.STRING_TEXT, token.getType());
        assertEquals("This {is a test", token.getValue());

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testStringTextInvalidEscapingThrows() {
        assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("This is a te{st").nextToken());
    }

    @Test
    public void testForTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$FOR a 1 -1.1$}");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.TAG_START, token.getType());
        assertEquals("{$", token.getValue());

        lexer.setState(SmartScriptLexerState.TAG);

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.IDENTIFIER, token.getType());
        assertEquals("FOR", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.INTEGER, token.getType());
        assertEquals(1, token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.DOUBLE, token.getType());
        assertEquals(-1.1, token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.TAG_END, token.getType());
        assertEquals("$}", token.getValue());
        lexer.setState(SmartScriptLexerState.TEXT);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagEmpty() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$=$}");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.TAG_START, token.getType());
        assertEquals("{$", token.getValue());

        lexer.setState(SmartScriptLexerState.TAG);

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.IDENTIFIER, token.getType());
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.TAG_END, token.getType());
        assertEquals("$}", token.getValue());
        lexer.setState(SmartScriptLexerState.TEXT);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagValidIdentifiers() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= variab_l5 Var8        $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.IDENTIFIER, token.getType());
        assertEquals("variab_l5", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.IDENTIFIER, token.getType());
        assertEquals("Var8", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagInvalidVariableNameStartingWithAnUnderscoreThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= _hello $}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagInvalidVariableNameStartingWithASymbolThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= !hello $}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagIntegerVariable() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= 7hello        $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.INTEGER, token.getType());
        assertEquals(7, token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.IDENTIFIER, token.getType());
        assertEquals("hello", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagInteger() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= 5 $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.INTEGER, token.getType());
        assertEquals(5, token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagNegativeInteger() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= -5 $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.INTEGER, token.getType());
        assertEquals(-5, token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagTooLongIntegerThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= 12345678987654321 $}");
        skipTagStart(lexer);

        assertThrows(NumberFormatException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagDouble() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= 5.4648 $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.DOUBLE, token.getType());
        assertEquals(5.4648, token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagNegativeDouble() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= -5.4648 $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.DOUBLE, token.getType());
        assertEquals(-5.4648, token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagDoubleDoubleDotThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= 2.2.5 $}");
        skipTagStart(lexer);

        assertThrows(NumberFormatException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagString() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= \"variab_l5 Var8       \" $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.STRING_TAG, token.getType());
        assertEquals("variab_l5 Var8       ", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagStringValidBackslashEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$=\"This \\\\is a test\"$}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.STRING_TAG, token.getType());
        assertEquals("This \\is a test", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagStringInvalidBackslashEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$=\"This \\is a test\"$}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagStringValidQuotationMarkEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$=\"This \\\"is\\\" a test\"$}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.STRING_TAG, token.getType());
        assertEquals("This \"is\" a test", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }


    @Test
    public void testEchoTagValidFunctionNames() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= @cosh@F_u_N123Ct_i_ON $}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.FUNCTION, token.getType());
        assertEquals("@cosh", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.FUNCTION, token.getType());
        assertEquals("@F_u_N123Ct_i_ON", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagInvalidFunctionNameStartingWithADigitThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= @3cosh $}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagInvalidFunctionNameStartingWithASymbolThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= @!cosh $}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagInvalidFunctionNameStartingWithAnUnderscoreThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= @_hello $}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testEchoTagValidOperators() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= +-/*^$}");
        skipTagStart(lexer);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.OPERATOR, token.getType());
        assertEquals("+", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.OPERATOR, token.getType());
        assertEquals("-", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.OPERATOR, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.OPERATOR, token.getType());
        assertEquals("*", token.getValue());

        token = lexer.nextToken();
        assertEquals(SmartScriptTokenType.OPERATOR, token.getType());
        assertEquals("^", token.getValue());

        skipTagEnd(lexer);
        checkEOF(lexer);
        checkIfNoMore(lexer);
    }

    @Test
    public void testEchoTagInvalidOperatorThrows() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= %$}");
        skipTagStart(lexer);

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    private void skipTagStart(SmartScriptLexer lexer) {
        lexer.nextToken();
        lexer.setState(SmartScriptLexerState.TAG);
        lexer.nextToken();
    }

    private void skipTagEnd(SmartScriptLexer lexer) {
        lexer.nextToken();
        lexer.setState(SmartScriptLexerState.TEXT);
    }

    private void checkEOF(SmartScriptLexer lexer) {
        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    private void checkIfNoMore(SmartScriptLexer lexer) {
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }
}
