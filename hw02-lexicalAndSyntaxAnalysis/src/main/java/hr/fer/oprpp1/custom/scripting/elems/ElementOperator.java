package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * The {@code ElementOperator} class represents an operator.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ElementOperator extends Element {
    /**
     * Symbol property of the current instance.
     */
    private final String symbol;

    /**
     * Creates a new instance of {@code ElementOperator} and sets its symbol value.
     *
     * @throws NullPointerException when the given {@code name} is {@code null}.
     * @param symbol string to be stored.
     */
    public ElementOperator(String symbol) {
        if (symbol == null) throw new NullPointerException("The symbol given cannot be null!");

        this.symbol = symbol;
    }

    /**
     * Fetches the value of the current instance.
     *
     * @return symbol value.
     */
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public String asText() {
        return this.symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementOperator)) return false;
        ElementOperator that = (ElementOperator) o;
        return this.symbol.equals(that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.symbol);
    }
}
