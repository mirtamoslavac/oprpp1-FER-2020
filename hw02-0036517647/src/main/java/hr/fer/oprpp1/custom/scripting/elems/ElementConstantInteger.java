package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 *  The {@code ElementConstantInteger} class represents a int constant.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ElementConstantInteger extends Element {

    /**
     * Value of the current instance.
     */
    private final int value;

    /**
     * Creates a new instance of {@code ElementConstantInteger} and sets its integer value.
     *
     * @param value integer to be stored.
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Fetches the value of the current instance.
     *
     * @return {@code value}.
     */
    public int getValue() {
        return this.value;
    }

    @Override
    public String asText() {
        return Integer.toString(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementConstantInteger)) return false;
        ElementConstantInteger that = (ElementConstantInteger) o;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
