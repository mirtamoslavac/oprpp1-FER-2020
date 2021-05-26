package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * The {@code ElementFunction} class represents a function by its name.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ElementFunction extends Element {
    /**
     * Name property of the current instance.
     */
    private final String name;

    /**
     * Creates a new instance of {@code ElementFunction} and sets its name.
     *
     * @throws NullPointerException when the given {@code name} is {@code null}.
     * @param name string to be stored.
     */
    public ElementFunction(String name) {
        if (name == null) throw new NullPointerException("The name given cannot be null!");

        this.name = name;
    }

    /**
     * Fetches the value of the current instance.
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
        if (!(o instanceof ElementFunction)) return false;
        ElementFunction that = (ElementFunction) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
