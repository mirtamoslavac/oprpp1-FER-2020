package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

/**
 * The {@code TextNode} class represents a piece of textual data.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class TextNode extends Node {

    /**
     * Textual data.
     */
    private final String text;

    /**
     * Creates a new instance of {@code TextNode} with the given piece of textual data.
     *
     * @param text textual data to be stored.
     * @throws NullPointerException when the given {@code text} is {@code null}.
     */
    public TextNode(String text) {
        if (text == null) throw new NullPointerException("The given text is null!");

        this.text = text;
    }

    /**
     * Fetches the the textual data of the current {@code TextNode} instance.
     *
     * @return value of {@code text}.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the string representation of the current {@code TextNode} instance, matching the original input, while taking into consideration the escaped characters ({@code \}
     * and <code>{</code>).
     *
     * @return string representation of the current {@code TextNode} instance.
     */
    @Override
    public String toString() {
        return this.text.replace("\\", "\\\\").replace("{", "\\{");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextNode)) return false;
        TextNode textNode = (TextNode) o;
        return this.text.equals(textNode.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text);
    }
}
