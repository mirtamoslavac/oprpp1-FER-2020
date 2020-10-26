package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;

import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class SmartScriptParserTest {

    private String readExample(int n) {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
            if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
            byte[] data = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")).readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch(IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }

    private String readFromExampleFolder(int n) {
        try {
            byte[] data = Files.newInputStream(Paths.get("examples/doc"+n+".txt")).readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch(IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }

    @Test
    void testCheckSingleTextNode() {
        DocumentNode root = new SmartScriptParser(readExample(1)).getDocumentNode();
        assertTrue(root.getChild(0) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(1));
    }

    @Test
    void testCheckSingleTextNodeWithEscapedOpenCurlyBracket() {
        DocumentNode root = new SmartScriptParser(readExample(2)).getDocumentNode();
        assertTrue(root.getChild(0) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(1));
    }

    @Test
    void testCheckSingleTextNodeWithEscapedBackslashAndOpenCurlyBracket() {
        DocumentNode root = new SmartScriptParser(readExample(3)).getDocumentNode();
        assertTrue(root.getChild(0) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(1));
    }

    @Test
    void testIllegalEscapeWithinATextNode1Throws() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readExample(4)));
    }

    @Test
    void testIllegalEscapeWithinATextNode2Throws() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readExample(5)));
    }

    @Test
    void testMultipleRowsNoExtraNewlineWithinEchoNodeString() {
        DocumentNode root = new SmartScriptParser(readExample(6)).getDocumentNode();
        assertEquals(root.numberOfChildren(), 3);
        assertTrue(root.getChild(0) instanceof TextNode);
        assertTrue(root.getChild(1) instanceof EchoNode);
        assertTrue(root.getChild(2) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(4));
    }

    @Test
    void testMultipleRowsExtraNewlineWithinEchoNodeString() {
        DocumentNode root = new SmartScriptParser(readExample(7)).getDocumentNode();
        assertEquals(root.numberOfChildren(), 3);
        assertTrue(root.getChild(0) instanceof TextNode);
        assertTrue(root.getChild(1) instanceof EchoNode);
        assertTrue(root.getChild(2) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(4));
    }

    @Test
    void testMultipleRowsIllegalOpenCurlyBracketEscapeWithinEchoNodeStringThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readExample(8)));
    }

    @Test
    void testIllegalEscapeAndUnknownTokenTypeBeginningWithinATagThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readExample(9)));
    }

    @Test
    void testMultipleForTagsAndNestedEchoTags() {
        DocumentNode root = new SmartScriptParser(readFromExampleFolder(1)).getDocumentNode();
        assertEquals(root.numberOfChildren(), 4);

        assertTrue(root.getChild(0) instanceof TextNode);

        assertTrue(root.getChild(1) instanceof ForLoopNode);
        assertTrue(root.getChild(1).getChild(0) instanceof TextNode);
        assertTrue(root.getChild(1).getChild(1) instanceof EchoNode);
        assertTrue(root.getChild(1).getChild(2) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(1).getChild(3));

        assertTrue(root.getChild(2) instanceof TextNode);

        assertTrue(root.getChild(3) instanceof ForLoopNode);
        assertTrue(root.getChild(3).getChild(0) instanceof TextNode);
        assertTrue(root.getChild(3).getChild(1) instanceof EchoNode);
        assertTrue(root.getChild(3).getChild(2) instanceof TextNode);
        assertTrue(root.getChild(3).getChild(3) instanceof EchoNode);
        assertTrue(root.getChild(3).getChild(4) instanceof TextNode);
        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(3).getChild(5));

        assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(4));
    }

    @Test
    void testTooFewArgumentsInForTagThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(2)));
    }

    @Test
    void testTooManyArgumentsInForTagThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(3)));
    }

    @Test
    void testMoreEndTagsThanForTagsThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(4)));
    }

    @Test
    void testMoreForTagsThanEndTagsThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(5)));
    }

    @Test
    void testInvalidArgumentsInForTagThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(6)));
    }

    @Test
    void testMissingTagEndingThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(7)));
    }

    @Test
    void testMissingEqualsInEchoTagThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(8)));
    }

    @Test
    void testInvalidTagName1Throws() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(9)));
    }

    @Test
    void testInvalidTagName2Throws() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(10)));
    }

    @Test
    void testMissingTagBeginningThrows() {
        assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(readFromExampleFolder(11)));
    }
}
