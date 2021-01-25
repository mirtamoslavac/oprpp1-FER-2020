package hr.fer.oprpp1.gui.charts;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * The {@code BarChartComponent} class represents a {@link JComponent} extension that contains a drawn bar chart.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class BarChartComponent extends JComponent {
    @java.io.Serial
    private static final long serialVersionUID = 1029384756758493021L;
    /**
     * The arrow colour on the bar chart.
     */
    private static final Color ARROW_COLOUR = Color.decode("#97A3DA");
    /**
     * The colour of the bars on the bar chart.
     */
    private static final Color BAR_COLOUR = Color.decode("#9EA9ED");
    /**
     * The colour of bars' border on the bar chart.
     */
    private static final Color BAR_BORDER_COLOUR = Color.decode("#474978");
    /**
     * The colour of the grid on the bar chart.
     */
    private static final Color GRID_COLOUR = Color.decode("#CCCCFF");
    /**
     * The colour of the coordinate lines on the bar chart.
     */
    private static final Color COORDINATE_COLOUR = Color.decode("#1F1D3F");
    /**
     * The colour of descriptions of the different axes on the bar chart.
     */
    private static final Color AXIS_DESCRIPTION_COLOUR = Color.decode("#292654");
    /**
     * The background colour of the bar chart.
     */
    private static final Color BACKGROUND_COLOUR = Color.decode("#CCCCFF");
    /**
     * The colour of shade thrown by the bars on the bar chart.
     */
    private static final Color SHADE_COLOUR = new Color(128, 128, 128, 128);

    /**
     * The bar chart that is to be drawn.
     */
    private final BarChart barChart;

    /**
     * Creates a new {@code BarChartComponent} instance.
     *
     * @param barChart the bar chart that is to be drawn.
     * @throws NullPointerException when the given {@code barChart} is {@code null}.
     */
    public BarChartComponent(BarChart barChart) {
        this.barChart = Objects.requireNonNull(barChart, "The given bar chart object cannot be null!");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        Insets ins = getInsets();
        Dimension dim = getSize();
        Rectangle availableArea = new Rectangle(ins.left, ins.top, dim.width - ins.left - ins.right, dim.height - ins.top - ins.bottom);

        if (isOpaque()) {
            graphics2D.setColor(BACKGROUND_COLOUR);
            graphics2D.fillRect(availableArea.x, availableArea.y, availableArea.width, availableArea.height);
        }
        graphics2D.setColor(getForeground());

        int textHeight = fontMetrics.getHeight();
        int totalYAxisDistance = textHeight * 2 + fontMetrics.stringWidth(Integer.toString(barChart.getYMax())) * 2;
        int totalXAxisDistance = textHeight * 4;
        int gapAndArrowSize = textHeight / 2;

        XYValue zero = new XYValue(totalYAxisDistance, availableArea.height - (totalXAxisDistance + gapAndArrowSize));
        int xAxisLength = availableArea.width - totalXAxisDistance - textHeight;
        int yAxisLength = availableArea.height - totalYAxisDistance - textHeight * 2;

        drawGrid(graphics2D, availableArea, xAxisLength, yAxisLength, gapAndArrowSize, zero);
        drawAxes(graphics2D, fontMetrics, availableArea, textHeight, totalXAxisDistance, totalYAxisDistance, xAxisLength, yAxisLength, gapAndArrowSize, zero);
        drawBars(graphics2D, textHeight, xAxisLength, yAxisLength, zero);
    }

    /**
     * Draws the grid of the current bar chart.
     *
     * @param graphics2D {@link Graphics2D} instance used for drawing on the component.
     * @param availableArea the entire area of the component.
     * @param xAxisLength the length of the x-axis.
     * @param yAxisLength the length of the y-axis.
     * @param gapAndArrowSize referent size for spacing out elements.
     * @param zero zero of the drawn coordinate system.
     */
    private void drawGrid(Graphics2D graphics2D, Rectangle availableArea, int xAxisLength, int yAxisLength, int gapAndArrowSize, XYValue zero) {
        int zeroX = zero.getX();
        int zeroY = zero.getY();
        int coordinateValue, offset;
        
        graphics2D.setColor(GRID_COLOUR);
        
        for (int i = 1, size = barChart.getPoints().size(); i <= size; i++) {
            offset = (int)(i * 1. / size * xAxisLength);
            coordinateValue = zeroX + offset;
            graphics2D.drawLine(coordinateValue, zeroY, coordinateValue, gapAndArrowSize);
        }

        for (int step = this.barChart.getYStep(), i = 0, amount = this.barChart.getYMax() / step; i <= amount; i++) {
            offset = (int)(i * 1. / amount * yAxisLength);
            coordinateValue = zeroY - offset;
            graphics2D.drawLine(zeroX, coordinateValue, availableArea.width - gapAndArrowSize, coordinateValue);
        }
    }

    /**
     * Draws axes and all relevant info of the current bar chart.
     *
     * @param graphics2D {@link Graphics2D} instance used for drawing on the component.
     * @param fontMetrics information about the font of the component.
     * @param availableArea the entire area of the component.
     * @param textHeight height of the used as a reference while drawing.
     * @param totalXAxisDistance distance from the bottom of the component to the x-axis.
     * @param totalYAxisDistance distance from the left side of the component to the y-axis.
     * @param xAxisLength the length of the x-axis.
     * @param yAxisLength the length of the y-axis.
     * @param gapAndArrowSize referent size for spacing out elements.
     * @param zero zero of the drawn coordinate system.
     */
    private void drawAxes(Graphics2D graphics2D, FontMetrics fontMetrics, Rectangle availableArea, int textHeight, int totalXAxisDistance, int totalYAxisDistance,
                          int xAxisLength, int yAxisLength, int gapAndArrowSize, XYValue zero) {
        double halfTheReferentSize = (int)ceil(textHeight / 4.);
        int zeroX = zero.getX();
        int zeroY = zero.getY();
        
        graphics2D.setColor(COORDINATE_COLOUR);
        graphics2D.drawLine(zeroX, zeroY, availableArea.width - gapAndArrowSize, zeroY);

        graphics2D.setColor(ARROW_COLOUR);
        int[] xPoints = new int[]{availableArea.width - (int)halfTheReferentSize,
                (int)(availableArea.width - gapAndArrowSize + textHeight / 2 * cos(PI / 4 + PI / 2)),
                (int)(availableArea.width - gapAndArrowSize + textHeight / 2 * cos(PI * 3 / 4 + PI / 2))};
        int[] yPoints = new int[]{zeroY, 
                (int)(zeroY + textHeight / 2 * sin(PI / 4 + PI / 2)),
                (int)(zeroY + textHeight / 2 * sin(PI * 3/ 4 + PI / 2))};
        graphics2D.fillPolygon(xPoints, yPoints ,3);
        
        
        graphics2D.setColor(COORDINATE_COLOUR);
        graphics2D.drawLine(zeroX, zeroY, zeroX, gapAndArrowSize);
        
        graphics2D.setColor(ARROW_COLOUR);
        xPoints = new int[]{zeroX,
                (int)(zeroX + gapAndArrowSize * cos(PI / 4)),
                (int)(zeroX + gapAndArrowSize * cos(PI * 3 / 4))};
        yPoints = new int[]{ (int)ceil(textHeight / 4.),
                (int)(halfTheReferentSize + textHeight * sin(PI / 4)),
                (int)(halfTheReferentSize + textHeight * sin(PI * 3 / 4))};
        graphics2D.fillPolygon(xPoints, yPoints ,3);


        Font plainFont = fontMetrics.getFont();
        Font boldFont = plainFont.deriveFont(Font.BOLD);
        
        AffineTransform oldTransform = graphics2D.getTransform();
        graphics2D.setFont(boldFont);
        graphics2D.setColor(AXIS_DESCRIPTION_COLOUR);

        int centerX = (availableArea.width + zeroX) / 2;
        graphics2D.drawString(this.barChart.getXDescription(), centerX - fontMetrics.stringWidth(barChart.getXDescription()) / 2, (int)(zeroY + 3.5 * textHeight));

        AffineTransform at = new AffineTransform();
        at.rotate(-PI / 2);
        graphics2D.setTransform(at);
        int centerY = ((availableArea.height + totalXAxisDistance) + gapAndArrowSize) / 2;
        graphics2D.drawString(this.barChart.getYDescription(), - (centerY + fontMetrics.stringWidth(barChart.getYDescription())),
                (int)(totalXAxisDistance - 2.5 * textHeight));
        graphics2D.setTransform(oldTransform);
        graphics2D.setFont(plainFont);


        List<Integer> xValues = this.barChart.getPoints().stream().map(XYValue::getX).sorted().collect(Collectors.toList());
        int referentSizeLarger = (int)ceil(textHeight * 1.5);

        for (int i = 0, size = this.barChart.getPoints().size(); i <= size; i++) {
            int offset = (int)(i * 1. / size * xAxisLength);
            graphics2D.drawLine(zeroX + offset, zeroY, zeroX + offset, zeroY + gapAndArrowSize);

            if (i != 0)  {
                String number = Integer.toString(xValues.get(i - 1));
                graphics2D.setFont(boldFont);
                graphics2D.drawString(number, (int)(zeroX + ((i*2. - 1)/(i*2)) * offset - fontMetrics.stringWidth(number) / 2),zeroY + referentSizeLarger);
                graphics2D.setFont(plainFont);
            }
        }

        int referentSizeSmaller = (int)ceil(textHeight / 4.);
        for (int step = this.barChart.getYStep(), i = 0, y = this.barChart.getYMin(), amount = this.barChart.getYMax() / step; i <= amount; i++) {
            int yOffset = zeroY - (int)(i * 1. / amount * yAxisLength);
            graphics2D.drawLine(zeroX, yOffset, zeroX - gapAndArrowSize, yOffset);
            String number = Integer.toString(y + step * i);
            graphics2D.setFont(boldFont);
            graphics2D.drawString(number, totalYAxisDistance - textHeight - fontMetrics.stringWidth(number), yOffset + referentSizeSmaller);
            graphics2D.setFont(plainFont);
        }
    }

    /**
     * Draws the bars of the current bar chart.
     *
     * @param graphics2D {@link Graphics2D} instance used for drawing on the component.
     * @param textHeight height of the used as a reference while drawing.
     * @param xAxisLength the length of the x-axis.
     * @param yAxisLength the length of the y-axis.
     * @param zero zero of the drawn coordinate system.
     */
    private void drawBars(Graphics2D graphics2D, int textHeight, int xAxisLength, int yAxisLength, XYValue zero) {
        int zeroX = zero.getX();
        int zeroY = zero.getY();
        
        List<XYValue> sortedPoints = barChart.getPoints().stream().sorted(Comparator.comparingInt(XYValue::getX)).collect(Collectors.toList());
        int referentSizeSmaller = (int)ceil(textHeight / 4.);

        for (int i = 0, size = sortedPoints.size(), amount = this.barChart.getYMax(), minY = this.barChart.getYMin(), width = (int)(1. / size * xAxisLength); i < size; i++) {
            int xOffset = zeroX + (int)(i * 1. / size * xAxisLength);
            int yOffset = (int)((sortedPoints.get(i).getY() - minY) * 1. / (amount - minY) * yAxisLength);

            graphics2D.setColor(SHADE_COLOUR);
            graphics2D.fillRect(xOffset + referentSizeSmaller, zeroY - yOffset + referentSizeSmaller, width, yOffset - referentSizeSmaller);

            graphics2D.setColor(BAR_COLOUR);
            graphics2D.fillRect(xOffset, zeroY - yOffset, width, yOffset);

            graphics2D.setColor(BAR_BORDER_COLOUR);
            graphics2D.drawRect(xOffset, zeroY - yOffset, width, yOffset);
        }
    }
}



