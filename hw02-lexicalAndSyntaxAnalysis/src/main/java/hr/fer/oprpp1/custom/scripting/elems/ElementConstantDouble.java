package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * The {@code ElementConstantDouble} class represents a double constant.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ElementConstantDouble extends Element {
    /**
     * Value of the current instance.
     */
    private final double value;

    /**
     * Creates a new instance of {@code ElementConstantDouble} and sets its double value.
     *
     * @param value double to be stored.
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Fetches the value of the current instance.
     *
     * @return {@code value}.
     */
    public double getValue() {
        return this.value;
    }

    @Override
    public String asText() {
        return Double.toString(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementConstantDouble)) return false;
        ElementConstantDouble that = (ElementConstantDouble) o;
        return Double.compare(that.value, this.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
