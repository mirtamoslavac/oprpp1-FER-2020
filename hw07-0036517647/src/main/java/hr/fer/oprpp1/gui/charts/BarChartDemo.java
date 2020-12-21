package hr.fer.oprpp1.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * The {@code BarChartDemo} class is a swing application that draws a bar chart from the information received through the input text file.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class BarChartDemo extends JFrame {
    @java.io.Serial
    private static final long serialVersionUID = 3956712480588211289L;
    /**
     * The number of rows within the text file that are to be processed.
     */
    private static final int CONSIDERED_ROWS = 6;
    /**
     * The width of the bar chart representation.
     */
    private static final int DISPLAY_WIDTH = 720;
    /**
     * The height of the bar chart representation.
     */
    private static final int DISPLAY_HEIGHT = 480;

    /**
     * Creates a new {@code BarChartDemo} instance.
     *
     * @param barChart the bar chart that is to be drawn.
     * @param path the path of the input text file.
     */
    public BarChartDemo(BarChart barChart, String path) {
        super();
        Objects.requireNonNull(barChart, "The given bar chart cannot be null!");
        Objects.requireNonNull(path, "The given path string cannot be null!");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bar chart demo");

        this.initGUI(barChart, path);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Initializes the bar chart GUI.
     *
     * @param barChart the bar chart that is to be drawn.
     * @param path the path of the input text file.
     * @throws NullPointerException when the given {@code barChart} or {@code path} are {@code null}.
     */
    private void initGUI(BarChart barChart, String path) {
        Objects.requireNonNull(barChart, "The given bar chart cannot be null!");
        Objects.requireNonNull(path, "The given path string cannot be null!");

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        cp.add(new JLabel(path, SwingConstants.CENTER), BorderLayout.PAGE_START);
        cp.add(new BarChartComponent(barChart), BorderLayout.CENTER);
    }

    /**
     * Starts the bar chart application.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("A single argument is expected, got " + args.length + "!");

        final String xDescription, yDescription;
        final List<XYValue> points;
        final int yMin, yMax, yStep;

        String row;
        List<String> rows = new ArrayList<>();
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8))) {
            while (((row = reader.readLine()) != null) && reader.getLineNumber() <= CONSIDERED_ROWS) rows.add(row);
            if (rows.size() < CONSIDERED_ROWS) throw new IllegalArgumentException("The file must contain at least six lines, got " + rows.size() + "!");

            xDescription = rows.get(0).strip();
            yDescription = rows.get(1).strip();
            points = parsePoints(rows.get(2).strip());
            yMin = parseInt(rows.get(3).strip());
            yMax = parseInt(rows.get(4).strip());
            yStep = parseInt(rows.get(5).strip());

            SwingUtilities.invokeLater(() -> new BarChartDemo(new BarChart(points, xDescription, yDescription, yMin, yMax, yStep),
                    Paths.get(args[0]).toAbsolutePath().normalize().toString()).setVisible(true));
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find a file at the given path!");
        } catch (IOException e) {
            System.out.println("Cannot read from the given path!");
        } catch (NumberFormatException e) {
            System.out.println("Cannot parse given data into a number!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Parses the given {@code pointsString} into {@link XYValue} instances.
     *
     * @param pointsString string that is to be parsed.
     * @throws IllegalArgumentException when the number of coordinates for a point does not equal 2.
     * @throws NullPointerException when the given {@code pointsString} is {@code null}.
     * @return list of parsed {@link XYValue} instances.
     */
    private static List<XYValue> parsePoints(String pointsString) {
        String[] pointsSplit = Objects.requireNonNull(pointsString, "The given row cannot be null!").split("\\s+");
        List<XYValue> points = new ArrayList<>();

        Arrays.stream(pointsSplit).forEach(string -> {
            String[] coordinates = string.split(",");
            if(coordinates.length != 2)
                throw new IllegalArgumentException("Invalid number of coordinates within the given point! Expected two, got " + coordinates.length + "!");

            points.add(new XYValue(parseInt(coordinates[0]), parseInt(coordinates[1])));
        });

        return points;
    }
}
