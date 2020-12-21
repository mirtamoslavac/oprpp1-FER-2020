package hr.fer.oprpp1.gui.calc.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The {@code CalcButton} class represents a general button of the calculator. It extends the {@link JButton}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CalcButton extends JButton {
    @java.io.Serial
    private static final long serialVersionUID = 3857539286478888322L;
    /**
     * The background colour of the button.
     */
    public static final Color BUTTON_COLOUR = Color.decode("#A4C27E");
    /**
     * The colour of the button's border.
     */
    public static final Color BUTTON_BORDER_COLOUR = Color.decode("#6D8257");
    /**
     * The thickness of the button's border.
     */
    private static final int BUTTON_BORDER_THICKNESS = 1;

    /**
     * Creates a new {@code CalcButton} instance.
     *
     * @param text text that is to be written within the button.
     * @param action action listener that contains the action that is executed when the button is pressed.
     */
    public CalcButton(String text, ActionListener action) {
        super(text);
        initGUI();
        addActionListener(action);
    }

    /**
     * Initializes the GUI.
     */
    protected void initGUI() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBackground(BUTTON_COLOUR);
        setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOUR, BUTTON_BORDER_THICKNESS));
    }



}
