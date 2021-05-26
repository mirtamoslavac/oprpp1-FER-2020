package hr.fer.oprpp1.gui.calc.components;

import java.awt.event.ActionListener;

/**
 * The {@code DigitButton} class represents a button with a respective digit. It extends the {@link CalcButton}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class DigitButton extends CalcButton {
    @java.io.Serial
    private static final long serialVersionUID = 4444753337465939332L;

    /**
     * Creates a new {@code DigitButton} instance.
     *
     * @param digit number that is to be written within the button.
     * @param listener action listener that contains the action that is executed when the button is pressed.
     */
    public DigitButton(String digit, ActionListener listener) {
        super(digit, listener);
    }

    @Override
    protected void initGUI() {
        super.initGUI();
        setFont(this.getFont().deriveFont(30f));
    }
}
