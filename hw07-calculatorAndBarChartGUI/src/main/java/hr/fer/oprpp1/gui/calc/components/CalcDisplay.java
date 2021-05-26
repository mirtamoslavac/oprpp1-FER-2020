package hr.fer.oprpp1.gui.calc.components;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code CalcDisplay} class is an extension of {@link JLabel} that represents a calculator's display with a specific value.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CalcDisplay extends JLabel {
    @java.io.Serial
    private static final long serialVersionUID = 8263534211112345675L;
    /**
     * The background colour of the display.
     */
    private static final Color DISPLAY_COLOUR = Color.decode("#6CBD9E");
    /**
     * The colour of the display's border.
     */
    private static final Color DISPLAY_BORDER_COLOUR = Color.decode("#476B5D");
    /**
     * The thickness of the display's border.
     */
    private static final int DISPLAY_BORDER_THICKNESS = 1;
    /**
     * The font size of the display.
     */
    private static final float DISPLAY_FONT_SIZE = 30f;

    /**
     * Creates a new {@code CalcDisplay} instance with the given {@code displayValue}.
     *
     * @param displayValue value to display on the calculator display.
     */
    public CalcDisplay(String displayValue) {
        super(displayValue);
        this.initGUI();
    }

    /**
     * Initializes the display's visuals.
     */
    private void initGUI() {
        setHorizontalAlignment(SwingConstants.RIGHT);
        setVerticalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setBackground(DISPLAY_COLOUR);
        setBorder(BorderFactory.createLineBorder(DISPLAY_BORDER_COLOUR, DISPLAY_BORDER_THICKNESS));
        setFont(this.getFont().deriveFont(DISPLAY_FONT_SIZE));
    }
}
