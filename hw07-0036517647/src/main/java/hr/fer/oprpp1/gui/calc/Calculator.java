package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.components.*;
import hr.fer.oprpp1.gui.calc.model.CalcModel;
import hr.fer.oprpp1.gui.calc.model.CalcModelImpl;
import hr.fer.oprpp1.gui.calc.model.CalculatorInputException;
import hr.fer.oprpp1.gui.layouts.CalcLayout;
import hr.fer.oprpp1.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

import static java.lang.Math.*;

/**
 * The {@code Calculator} class is a swing application containing an implementation of a simple calculator.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Calculator extends JFrame {
    @java.io.Serial
    private static final long serialVersionUID = 924801882472464911L;
    /**
     * The gap between all the buttons and the display as well.
     */
    private static final int GAP_SIZE = 5;
    /**
     * {@link CalcModel} instance used for the inner workings of the calculator.
     */
    private final CalcModel calcModel;
    /**
     * The stack used for {@code push} and {@code pull} operations within the calculator.
     */
    private final Stack<Double> storageStack;

    /**
     * Collection of (mostly unary) operations that have inverse counterparts available through the calculator.
     */
    private final Map<String, InverseButton> invOperations;
    /**
     * Collection of binary operations (without a inverse counterpart) available through the calculator.
     */
    private final Map<String, BinaryButton> binaryOperations;
    /**
     * Collection of options within the calculator.
     */
    private final Map<String, CalcButton> options;
    /**
     * Collection of remaining buttons that perform some action within the calculator.
     */
    private final Map<String, CalcButton> otherActions;

    /**
     * Creates a new {@code Calculator} instance using the given {@code calcModel}.
     *
     * @param calcModel the calculator model performing the operation.
     * @throws NullPointerException when the given {@code calcModel} is {@code null}.
     */
    public Calculator(CalcModel calcModel) {
        super();
        this.calcModel = calcModel;
        this.storageStack = new Stack<>();

        this.invOperations = new LinkedHashMap<>(Map.of(
                "2,1", new InverseButton("1/x", "1/x",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(1 / calcModel.getValue());
                        }, e -> {
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                    calcModel.setValue(1 / calcModel.getValue());
                }, calcModel),
                "3,1", new InverseButton("log", "10^x",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(log10(calcModel.getValue()));
                        }, e -> {
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                    calcModel.setValue(pow(10, calcModel.getValue()));
                }, calcModel),
                "4,1", new InverseButton("ln", "e^x",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(log(calcModel.getValue()));
                        }, e -> {
                    calcModel.setValue(pow(Math.E, calcModel.getValue()));
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                }, calcModel),
                "5,1", new InverseButton("x^n", "x^(1/n)", Math::pow, (double x, double n) -> pow(x, 1 / n), calcModel),
                "2,2", new InverseButton("sin", "arcsin",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(sin(calcModel.getValue()));
                        }, e -> {
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                    calcModel.setValue(asin(calcModel.getValue()));
                }, calcModel),
                "3,2", new InverseButton("cos", "arccos",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(cos(calcModel.getValue()));
                        }, e -> {
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                    calcModel.setValue(acos(calcModel.getValue()));
                }, calcModel),
                "4,2", new InverseButton("tan", "arctan",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(tan(calcModel.getValue()));
                        }, e -> {
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                    calcModel.setValue(atan(calcModel.getValue()));
                }, calcModel),
                "5,2", new InverseButton("ctg", "arcctg",
                        e -> {
                            if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                            calcModel.setValue(1 / tan(calcModel.getValue()));
                        }, e -> {
                    if (calcModel.hasFrozenValue()) throw new CalculatorInputException("The result has been frozen!");
                    calcModel.setValue(1 / atan(calcModel.getValue()));
                }, calcModel)));

        this.binaryOperations = new LinkedHashMap<>(Map.of(
                "2,6", new BinaryButton("/", (x, y) -> x / y, calcModel),
                "3,6", new BinaryButton("*", (x, y) -> x * y, calcModel),
                "4,6", new BinaryButton("-", (x, y) -> x - y, calcModel),
                "5,6", new BinaryButton("+", (x, y) -> x + y, calcModel)));

        this.options = new LinkedHashMap<>(Map.of(
                "1,7", new CalcButton("clr", e -> calcModel.clear()),
                "2,7", new CalcButton("reset", e -> calcModel.clearAll()),
                "3,7", new CalcButton("push", e -> this.storageStack.push(calcModel.getValue())),
                "4,7", new CalcButton("pop", e -> {
                    if (this.storageStack.isEmpty()) throw new CalculatorInputException("Cannot pop from an empty stack!");
                    calcModel.setValue(this.storageStack.pop());
                })));

        this.otherActions = new LinkedHashMap<>(Map.of(
                "1,6", new CalcButton("=", e -> {
                    DoubleBinaryOperator pendingOperation = calcModel.getPendingBinaryOperation();
                    if (pendingOperation != null) calcModel.setActiveOperand(calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
                    calcModel.setPendingBinaryOperation(null);
                    calcModel.setValue(calcModel.getActiveOperand());
                }),
                "5,4", new CalcButton("+/-", e -> calcModel.swapSign()),
                "5,5", new CalcButton(".", e -> calcModel.insertDecimalPoint())));

        setTitle("Java Calculator");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.initGUI();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Initializes the calculator GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(GAP_SIZE));

        addDisplay(cp);
        addDigitButtons(cp);
        addInvertibleOperations(cp);
        addToContainer(this.binaryOperations.entrySet(), cp);
        addToContainer(this.options.entrySet(), cp);
        addToContainer(this.otherActions.entrySet(), cp);
    }

    /**
     * Adds a {@link CalcDisplay} instance to the given {@code container}.
     *
     * @param container the container to which the {@link CalcDisplay} instance is to be added.
     * @throws NullPointerException when the given {@code container} is {@code null}.
     */
    private void addDisplay(Container container) {
        Objects.requireNonNull(container, "The given parent container cannot be null!");
        CalcDisplay display = new CalcDisplay(calcModel.toString());
        container.add(display, new RCPosition(1, 1));
        this.calcModel.addCalcValueListener(calcModel -> display.setText(calcModel.toString()));
    }

    /**
     * Adds {@link DigitButton} instances to the given {@code container}.
     *
     * @param container the container to which the {@link DigitButton} instances are to be added.
     * @throws NullPointerException when the given {@code container} is {@code null}.
     */
    private void addDigitButtons(Container container) {
        Objects.requireNonNull(container, "The given parent container cannot be null!");

        for (int n = 0, i = 5; i > 1; i--) {
            for (int j = 3; j < 6; n++, j++) {
                if (i == 5 && j != 3) break;
                int finalN = n;
                container.add(new DigitButton(Integer.toString(finalN), e -> calcModel.insertDigit(finalN)), new RCPosition(i, j));
            }
        }
    }

    /**
     * Adds {@link InverseButton} instances to the given {@code container}.
     *
     * @param container the container to which the {@link InverseButton} instances are to be added.
     * @throws NullPointerException when the given {@code container} is {@code null}.
     */
    private void addInvertibleOperations(Container container) {
        Objects.requireNonNull(container, "The given parent container cannot be null!");
        JCheckBox invertedCheckBox = new JCheckBox("Inv");
        invertedCheckBox.setHorizontalAlignment(SwingConstants.LEFT);
        invertedCheckBox.setVerticalAlignment(SwingConstants.CENTER);
        invertedCheckBox.setSelected(false);

        invertedCheckBox.addActionListener(listener -> {
            for (Map.Entry<String, InverseButton> invOperation : invOperations.entrySet()) {
                invOperation.getValue().invert();
            }
        });

        container.add(invertedCheckBox, new RCPosition(5, 7));

        addToContainer(this.invOperations.entrySet(), container);
    }

    /**
     * Adds already specified buttons to the {@code container}.
     *
     * @param entrySet  set containing information about the buttons.
     * @param container the container to which the buttons are to be added.
     * @param <V>       type of the buttons that are to be added to the container.
     * @throws NullPointerException when the given {@code container} or {@code entrySet} are {@code null}.
     */
    private <V extends Component> void addToContainer(Set<Map.Entry<String, V>> entrySet, Container container) {
        Objects.requireNonNull(entrySet, "The given entry set cannot be null!");
        Objects.requireNonNull(container, "The given parent container cannot be null!");
        for (Map.Entry<String, V> otherAction : entrySet) {
            container.add(otherAction.getValue(), otherAction.getKey());
        }
    }

    /**
     * Starts the calculator application.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());

        SwingUtilities.invokeLater(() -> new Calculator(new CalcModelImpl()).setVisible(true));
    }

    public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void handle(Throwable thrown) {
            handleException(Thread.currentThread().getName(), thrown);
        }

        public void uncaughtException(Thread thread, Throwable thrown) {
            handleException(thread.getName(), thrown);
        }

        protected void handleException(String threadName, Throwable thrown) {
            JOptionPane.showMessageDialog(null, thrown.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

}
