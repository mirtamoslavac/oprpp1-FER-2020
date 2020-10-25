package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Arrays;

/**
 * The {@code EchoNode} class represents a command which generates some textual output dynamically.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class EchoNode extends Node {
    /**
     * An array of {@code Element} instances of the current instance.
     */
    private final Element[] elements;

    /**
     * Creates a new {@code EchoNode} instance.
     *
     * @param elements elements within the {@code EchoNode}.
     * @throws NullPointerException when the given {@code elements} array is {@code null}.
     */
    public EchoNode(Element[] elements) {
        if (elements == null) throw new NullPointerException("Element array cannot be null!");

        this.elements = elements;
    }

    /**
     * Fetches the {@code elements} array of the current {@code EchoNode} instance.
     *
     * @return {@code elements} array.
     */
    public Element[] getElements() {
        return this.elements;
    }

    /**
     * Returns the string representation of the current {@code EchoNode} instance, matching the original input without the additional whitespace in front of the tag name, as
     * well as redundant whitespace between elements.
     *
     * @return string representation of the current {@code EchoNode} instance.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{$=");
        for (Element element: this.elements) {
            sb.append(element.asText()).append(" ");
        }
        sb.append("$}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EchoNode)) return false;
        EchoNode echoNode = (EchoNode) o;
        return Arrays.equals(this.elements, echoNode.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.elements);
    }
}
