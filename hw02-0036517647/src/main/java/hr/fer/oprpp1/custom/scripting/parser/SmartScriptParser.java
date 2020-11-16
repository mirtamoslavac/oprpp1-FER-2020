package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptToken;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

import java.util.Arrays;

/**
 * The {@code SmartScriptParser} class represents an implementation of a parser.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class SmartScriptParser {

    /**
     * {@link SmartScriptLexer} instance used by the current parser to tokenize characters.
     */
    private final SmartScriptLexer lexer;

    /**
     * {@link ObjectStack} instance used by the current parser to construct the document tree.
     */
    private final ObjectStack stack;

    /**
     * {@link DocumentNode} representing the root node of the document tree of the current parser.
     */
    private final DocumentNode documentNode;

    /**
     * Creates a new {@code SmartScriptParser} instance.
     * It also creates its own new {@link SmartScriptLexer} instance which enables it to build a document tree corresponding to the given {@code documentBody}.
     *
     * @param documentBody input text that is the basis for the creation of document tree of the current parser.
     * @throws NullPointerException when the given {@code documentBody} is {@code null}.
     * @throws SmartScriptParserException when the input text cannot be parsed due to an error that occurred during the parse.
     */
    public SmartScriptParser(String documentBody) {
        if (documentBody == null) throw new NullPointerException("The given document body cannot be null!");

        this.lexer = new SmartScriptLexer(documentBody);
        this.stack = new ObjectStack();

        try {
            this.documentNode = this.parseDocumentBody();
        } catch (Exception e) {
            throw new SmartScriptParserException(
                    e.getClass().getCanonicalName().equals("hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException") ?
                            e.getMessage() :
                            e.getClass().getCanonicalName() +  ": " + e.getMessage());
        }
    }

    /**
     * Returns the document node of the current parser.
     *
     * @return {@link DocumentNode} instance of the parser.
     */
    public DocumentNode getDocumentNode() {
        return this.documentNode;
    }

    private DocumentNode parseDocumentBody() {
        this.stack.push(new DocumentNode());

        SmartScriptToken currentToken = this.lexer.nextToken();
        while (currentToken.getType() != SmartScriptTokenType.EOF) {
            switch (currentToken.getType()) {
                case STRING_TEXT -> ((Node)this.stack.peek()).addChildNode(new TextNode((String)currentToken.getValue()));
                case TAG_START -> this.lexer.setState(SmartScriptLexerState.TAG);
                case IDENTIFIER -> this.parseTag(currentToken);
                default -> throw new SmartScriptParserException("Invalid token type!");
            }

            currentToken = this.lexer.nextToken();
        }

        if (this.stack.isEmpty()) {
            throw new SmartScriptParserException("More {$END$} tags than opened non-empty tags!");
        }

        return (DocumentNode)stack.pop();
    }

    private void parseTag(SmartScriptToken currentToken) {

        switch ((String)currentToken.getValue()) {
            case "=" -> ((Node)this.stack.peek()).addChildNode(this.parseEchoTag());
            case "FOR" -> {
                ForLoopNode forLoopNode = this.parseForTag();
                ((Node)this.stack.peek()).addChildNode(forLoopNode);
                this.stack.push(forLoopNode);
            }
            case "END" -> {
                if (this.lexer.nextToken().getType() != SmartScriptTokenType.TAG_END)
                    throw new SmartScriptParserException("The END tag name should be immediately followed by specified tag ending!");
                this.stack.pop();
            }
            default -> throw new SmartScriptParserException("Invalid tag name!");
        }

        this.lexer.setState(SmartScriptLexerState.TEXT);
    }



    private EchoNode parseEchoTag() {
        ArrayIndexedCollection elements = new ArrayIndexedCollection();

        SmartScriptToken currentToken = this.lexer.nextToken();

        while (currentToken.getType() != SmartScriptTokenType.TAG_END) {
            elements.add(
                    switch (currentToken.getType()) {
                        case EOF -> throw new SmartScriptParserException("The =-tag was not closed with a specified tag ending!");
                        case IDENTIFIER -> new ElementVariable((String)currentToken.getValue());
                        case INTEGER -> new ElementConstantInteger((int)currentToken.getValue());
                        case DOUBLE -> new ElementConstantDouble((double)currentToken.getValue());
                        case STRING_TAG -> new ElementString((String)currentToken.getValue());
                        case FUNCTION -> new ElementFunction((String)currentToken.getValue());
                        case OPERATOR -> new ElementOperator((String)currentToken.getValue());
                        default -> throw new SmartScriptParserException("Invalid token type!");
            });

            currentToken = this.lexer.nextToken();
        }

        return new EchoNode(Arrays.copyOf(elements.toArray(), elements.size(), Element[].class));

    }

    private ForLoopNode parseForTag() {
        ArrayIndexedCollection elements = new ArrayIndexedCollection();

        SmartScriptToken currentToken = this.lexer.nextToken();
        while (currentToken.getType() != SmartScriptTokenType.TAG_END) {
            elements.add(
                    switch (currentToken.getType()) {
                        case EOF -> throw new SmartScriptParserException("The FOR tag was not closed with a specified tag ending!");
                        case IDENTIFIER -> new ElementVariable((String)currentToken.getValue());
                        case INTEGER -> new ElementConstantInteger((int)currentToken.getValue());
                        case DOUBLE -> new ElementConstantDouble((double)currentToken.getValue());
                        case STRING_TAG -> new ElementString((String)currentToken.getValue());
                        case FUNCTION -> new ElementFunction((String)currentToken.getValue());
                        case OPERATOR -> new ElementOperator((String)currentToken.getValue());
                        default -> throw new SmartScriptParserException("Invalid token type!");
                    }
            );

            currentToken = this.lexer.nextToken();
        }

        return new ForLoopNode((ElementVariable) elements.get(0), Arrays.copyOfRange(elements.toArray(), 1, elements.size(), Element[].class));
    }


}
