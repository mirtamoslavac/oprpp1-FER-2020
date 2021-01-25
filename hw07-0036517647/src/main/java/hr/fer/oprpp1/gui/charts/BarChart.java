package hr.fer.oprpp1.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * The {@code BarChart} class contains information relevant for drawing a certain bar chart.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class BarChart {
    /**
     * The points of the current bar chart.
     */
    private final List<XYValue> points;
    /**
     * The description of the x-axis of the current bar chart.
     */
    private final String xDescription;
    /**
     * The description of the y-axis of the current bar chart.
     */
    private final String yDescription;
    /**
     * Minimum y on the current bar chart.
     */
    private final int yMin;
    /**
     * Maximum y on the current bar chart.
     */
    private final int yMax;
    /**
     * Step between two y values on the current bar chart.
     */
    private final int yStep;

    /**
     * Creates a new {@code BarChart} instance.
     *
     * @param points points of the current bar chart.
     * @param xDescription x-axis description.
     * @param yDescription y-axis description.
     * @param yMin minimum y on the current bar chart
     * @param yMax maximum y on the current bar chart.
     * @param yStep step between two y values on the current bar chart.
     * @throws IllegalArgumentException when the given {@code points} is empty, {@code yMin} is negative or when the given {@code yMax} or any of the y values
     * within the {@code points} is smaller than {@code yMin}.
     * @throws NullPointerException the given {@code points}, any element of the {@code points} list, {@code xDescription} or {@code yDescription} are {@code null}.
     */
    public BarChart(List<XYValue> points, String xDescription, String yDescription, int yMin, int yMax, int yStep) {
        Objects.requireNonNull(points, "The given list of points cannot be null!");
        if (points.isEmpty()) throw new IllegalArgumentException("The given list of points is empty!");
        points.forEach(point -> Objects.requireNonNull(point, "The given point cannot be null!"));

        Objects.requireNonNull(xDescription, "The given description of the x-axis cannot be null!");
        Objects.requireNonNull(yDescription, "The given description of the y-axis cannot be null!");

        if (yMin < 0) throw new IllegalArgumentException("The given yMin cannot be negative!");
        if (yMax <= yMin) throw new IllegalArgumentException("The given yMax has to be larger than the given yMin!");
        points.stream()
                .map(XYValue::getY)
                .forEach(y -> {if(y < yMin) throw new IllegalArgumentException("The given y value cannot be smaller than the given yMin!");});

        this.points = points;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        this.yMin = yMin;
        this.yMax = (yMax - yMin) % yStep != 0 ? yMin + yStep * (((yMax - yMin) / yStep) + 1) : yMax;
        this.yStep = yStep;
    }

    /**
     * Fetches the points of the current bar chart.
     *
     * @return list of {@link XYValue} instances.
     */
    public List<XYValue> getPoints() {
        return this.points;
    }

    /**
     * Fetches the description of the x-axis of the current bar chart.
     *
     * @return x-axis description of the current bar chart.
     */
    public String getXDescription() {
        return xDescription;
    }

    /**
     * Fetches the description of the y-axis of the current bar chart.
     *
     * @return y-axis description of the current bar chart.
     */
    public String getYDescription() {
        return this.yDescription;
    }

    /**
     * Fetches the yMin of the current bar chart.
     *
     * @return yMin of the current bar chart.
     */
    public int getYMin() {
        return this.yMin;
    }

    /**
     * Fetches the yMax of the current bar chart.
     *
     * @return yMax of the current bar chart.
     */
    public int getYMax() {
        return this.yMax;
    }

    /**
     * Fetches the yStep of the current bar chart.
     *
     * @return yStep of the current bar chart.
     */
    public int getYStep() {
        return this.yStep;
    }
}
