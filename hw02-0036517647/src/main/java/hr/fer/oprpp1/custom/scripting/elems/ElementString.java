package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * The {@code ElementString} class represents a String by its name.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ElementString extends Element {
    /**
     * Value of the current instance.
     */
    private final String value;

    /**
     * Creates a new instance of {@code ElementString} and sets its string value.
     *
     * @param value string to be stored.
     * @throws NullPointerException when the given {@code value} is {@code null}.
     */
    public ElementString(String value) {
        if (value == null) throw new NullPointerException("The given text is null!");

        this.value = value;
    }

    /**
     * Fetches the value of the current instance.
     *
     * @return value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * Takes the escaped characters ({@code \} and {@code "}) into consideration.
     */
    @Override
    public String asText() {
        return "\"" + this.value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementString)) return false;
        ElementString that = (ElementString) o;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}