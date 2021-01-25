package hr.fer.oprpp1.gui.charts;

/**
 * The {@code XYValue} represents a point characterized by the values on the x- and y-axes.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class XYValue {
    /**
     * Value on the x-axis.
     */
    private final int x;
    /**
     * Value on the y-axis.
     */
    private final int y;

    /**
     * Creates a new {@code XYValue} instance.
     *
     * @param x value on the x-axis.
     * @param y value on the y-axis.
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Fetches the value of {@code x} of the current {@code XYValue} instance.
     *
     * @return value of x.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Fetches the value of {@code y} of the current {@code XYValue} instance.
     *
     * @return value of y.
     */
    public int getY() {
        return this.y;
    }
}
