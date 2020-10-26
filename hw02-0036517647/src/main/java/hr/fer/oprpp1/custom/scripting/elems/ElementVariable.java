package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * The {@code ElementVariable} class represents a String.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ElementVariable extends Element {
    /**
     * Name property of the current instance.
     */
    private final String name;

    /**
     * Creates a new instance of {@code ElementVariable} and sets its name.
     *
     * @throws NullPointerException when the given {@code name} is {@code null}.
     * @param name string to be stored.
     */
    public ElementVariable(String name) {
        if (name == null) throw new NullPointerException("The name given cannot be null!");

        this.name = name;
    }

    /**
     * Fetches the name of the current instance.
     *
     * @return {@code name} value.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String asText() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementVariable)) return false;
        ElementVariable that = (ElementVariable) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
