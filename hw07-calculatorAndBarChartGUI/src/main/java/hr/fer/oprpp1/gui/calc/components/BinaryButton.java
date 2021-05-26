package hr.fer.oprpp1.gui.calc.components;

import hr.fer.oprpp1.gui.calc.model.CalcModel;
import hr.fer.oprpp1.gui.calc.model.CalculatorInputException;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * The {@code BinaryButton} class represents a button that, when pressed, enables performing a certain binary operation. It extends the {@link CalcButton}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class BinaryButton extends CalcButton {
    @java.io.Serial
    private static final long serialVersionUID = 928723536838254112L;

    /**
     * Creates a new {@code BinaryButton} instance.
     *
     * @param text the text that is to be displayed.
     * @param binaryOperation action listener containing the binary operation that is to be performed when the button in the inverted state is pressed.
     * @param calcModel the calculator model performing the operation.
     */
    public BinaryButton(String text, DoubleBinaryOperator binaryOperation, CalcModel calcModel) {
        super(text, e -> {
            if (Objects.requireNonNull(calcModel, "The given calculator model cannot be null!").isActiveOperandSet()) {
                if(calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                double result = calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
                calcModel.freezeValue(Double.toString(result));
                calcModel.setActiveOperand(result);
            } else calcModel.setActiveOperand(calcModel.getValue());

            calcModel.setPendingBinaryOperation(Objects.requireNonNull(binaryOperation, "The given binary operation cannot be null!"));
            calcModel.clear();
        });
    }
}
