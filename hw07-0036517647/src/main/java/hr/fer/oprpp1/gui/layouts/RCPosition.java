package hr.fer.oprpp1.gui.layouts;

import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * The {@code RCPosition} models the position of a grid element with a defined row and column.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class RCPosition {
    /**
     * Index of the row.
     */
    private final int row;
    /**
     * Index of the column.
     */
    private final int column;

    /**
     * Creates a new {@code RCPosition} instance.
     * @param row row index.
     * @param column column index.
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Factory method for creating a new {@code RCPosition} instance from a parsed {@code text}.
     *
     * @param text string to be parsed.
     * @return new {@code RCPosition} instance.
     */
    public static RCPosition parse(String text) {
        Objects.requireNonNull(text, "The given text to be parsed cannot be null!");
        text = text.replaceAll("\\s+", "");

        if(!text.matches("^\\d,\\d$")) throw new IllegalArgumentException("Invalid syntax of a text that is to be parsed given!");

        String[] arguments = text.split(",");
        return new RCPosition(parseInt(arguments[0]), parseInt(arguments[1]));
    }

    /**
     * Fetches the row index of the current {@code RCPosition} instance.
     *
     * @return row index.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Fetches the column index of the current {@code RCPosition} instance.
     *
     * @return column index.
     */
    public int getColumn() {
        return this.column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RCPosition)) return false;
        RCPosition that = (RCPosition) o;
        return this.row == that.row &&
                this.column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.column);
    }
}
