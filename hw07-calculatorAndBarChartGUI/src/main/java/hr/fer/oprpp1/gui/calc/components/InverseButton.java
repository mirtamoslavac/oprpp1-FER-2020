package hr.fer.oprpp1.gui.calc.components;

import hr.fer.oprpp1.gui.calc.model.CalcModel;
import hr.fer.oprpp1.gui.calc.model.CalculatorInputException;

import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * The {@code InverseButton} class represents a button that toggles between certain math operations and their inverse counterparts. It extends the {@link CalcButton}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class InverseButton extends CalcButton {
    @java.io.Serial
    private static final long serialVersionUID = 2398579843625873222L;
    /**
     * The text within the button that is to be displayed regularly.
     */
    private final String nonInvertedText;
    /**
     * The text within the button that is to be displayed when inverse operations are toggled.
     */
    private final String invertedText;
    /**
     * Action listener containing the operation that is to be performed when the button in the regular state is pressed.
     */
    private final ActionListener nonInvertedOperation;
    /**
     * Action listener containing the operation that is to be performed when the button in the inverted state is pressed.
     */
    private final ActionListener invertedOperation;
    /**
     * The state of the button.
     */
    private boolean inverted;

    /**
     * Creates a new {@code InverseButton} instance for a unary operation.
     *
     * @param nonInvertedText the text that is to be displayed regularly.
     * @param invertedText the text that is to be displayed when inverse operations are toggled.
     * @param nonInvertedOperation action listener containing the unary operation that is to be performed when the button in the regular state is pressed.
     * @param invertedOperation action listener containing the unary operation that is to be performed when the button in the inverted state is pressed.
     * @param calcModel the calculator model performing the operation.
     */
    public InverseButton(String nonInvertedText, String invertedText, ActionListener nonInvertedOperation, ActionListener invertedOperation, CalcModel calcModel) {
        super(nonInvertedText, nonInvertedOperation);
        this.inverted = false;

        this.nonInvertedText = nonInvertedText;
        this.invertedText = invertedText;
        this.nonInvertedOperation = nonInvertedOperation;
        this.invertedOperation = invertedOperation;
    }

    /**
     * Creates a new {@code InverseButton} instance for a binary operation.
     *
     * @param nonInvertedText the text that is to be displayed regularly.
     * @param invertedText the text that is to be displayed when inverse operations are toggled.
     * @param nonInvertedOperation action listener containing the binary operation that is to be performed when the button in the regular state is pressed.
     * @param invertedOperation action listener containing the binary operation that is to be performed when the button in the inverted state is pressed.
     * @param calcModel the calculator model performing the operation.
     */
    public InverseButton(String nonInvertedText, String invertedText, DoubleBinaryOperator nonInvertedOperation, DoubleBinaryOperator invertedOperation, CalcModel calcModel) {
        this(nonInvertedText, invertedText, e -> {
            if (Objects.requireNonNull(calcModel, "The given calculator model cannot be null!").isActiveOperandSet()) {
                if(calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                double result = calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
                calcModel.freezeValue(Double.toString(result));
                calcModel.setActiveOperand(result);
            } else calcModel.setActiveOperand(calcModel.getValue());

            calcModel.setPendingBinaryOperation(Objects.requireNonNull(nonInvertedOperation, "The given binary operation cannot be null!"));
            calcModel.clear();
        }, e -> {
            if (Objects.requireNonNull(calcModel, "The given calculator model cannot be null!").isActiveOperandSet()) {
                if(calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                double result = calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
                calcModel.freezeValue(Double.toString(result));
                calcModel.setActiveOperand(result);
            } else calcModel.setActiveOperand(calcModel.getValue());

            calcModel.setPendingBinaryOperation(Objects.requireNonNull(invertedOperation, "The given binary operation cannot be null!"));
            calcModel.clear();
        }, calcModel);
    }

    /**
     * Inverts the current layout's buttons that have a respective inverse.
     */
    public void invert() {
        removeActionListener(this.inverted ? this.invertedOperation : this.nonInvertedOperation);
        addActionListener(this.inverted ? this.nonInvertedOperation : this.invertedOperation);
        setText(this.inverted ? this.nonInvertedText : this.invertedText);
        this.inverted = !this.inverted;
    }
}
