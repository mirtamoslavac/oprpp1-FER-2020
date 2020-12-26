package hr.fer.oprpp1.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import static java.lang.Character.isDigit;
import static java.lang.Double.*;

/**
 * The {@code CalcModelImpl} represents the implementation of a calculator model by implementing the {@link CalcModel}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CalcModelImpl implements CalcModel{
    /**
     * States whether the model is editable.
     */
    private boolean editable;
    /**
     * States whether the number within the model is positive or negative.
     */
    private boolean positive;
    /**
     * The inserted digits.
     */
    private String currentInput;
    /**
     * The number that can be derived from the {@code currentInput}.
     */
    private double currentValue;
    /**
     * Digits that were last set and to be shown on the calculator display.
     */
    private String frozenValue;
    /**
     * The operand that is currently active.
     */
    private Double activeOperand;
    /**
     * The currently active operation.
     */
    private DoubleBinaryOperator pendingOperation;

    private final List<CalcValueListener> listeners;

    /**
     * Default constructor that initializes the current {@code CalcModelImpl} instance.
     */
    public CalcModelImpl() {
        this.editable = true;
        this.positive = true;
        this.currentInput = "";
        this.currentValue = 0;
        this.frozenValue = null;
        this.activeOperand = null;
        this.pendingOperation = null;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        this.listeners.add(Objects.requireNonNull(l, "The given listener cannot be null!"));
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        this.listeners.remove(Objects.requireNonNull(l, "The given listener cannot be null!"));
    }

    @Override
    public double getValue() {
        return this.positive ? this.currentValue : parseDouble("-" + this.currentValue);
    }

    @Override
    public void setValue(double value) {
        this.editable = false;
        this.currentValue = Math.abs(value);
        this.positive = !(value < 0);
        this.currentInput = Double.toString(Math.abs(value));
        this.frozenValue = null;
        this.notifyRegisteredListeners();
    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public void clear() {
        this.editable = true;
        this.positive = true;
        this.currentInput = "";
        this.currentValue = 0.;

        this.notifyRegisteredListeners();
    }

    @Override
    public void clearAll() {
        this.clearActiveOperand();
        this.pendingOperation = null;
        this.frozenValue = null;
        this.clear();
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!this.isEditable()) throw new CalculatorInputException("The number's sign cannot be flipped if the calculator is not editable!");

        this.positive = !this.positive;
        this.frozenValue = null;
        this.notifyRegisteredListeners();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!this.isEditable()) throw new CalculatorInputException("A decimal point cannot be inserted if the calculator is not editable!");
        if (this.currentInput.contains(".")) throw new CalculatorInputException("The number already contains a decimal point!");
        if (this.currentInput.isEmpty()) throw new CalculatorInputException("No number had been entered previously!");

        String newInput = this.currentInput.isBlank()? "0." : (this.currentInput + '.').replaceFirst("^0+(?!(\\.|$))", "");
        try {
            double newValue = parseDouble(newInput);

            this.currentInput = newInput;
            this.currentValue = newValue;
            this.frozenValue = null;
            this.notifyRegisteredListeners();
        } catch (NumberFormatException | CalculatorInputException e) {
            throw new CalculatorInputException("The given value \"" + newInput + "\" cannot be parsed into a double!");
        }
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!this.isEditable()) throw new CalculatorInputException("A digit cannot be inserted if the calculator is not editable!");
        if (!isDigit(digit + 48)) throw new IllegalArgumentException("Cannot parse the given input to a digit! Expected 0-9, got " + digit + "!");
        String newInput = (this.currentInput + digit).replaceFirst("^0+(?!(\\.|$))", "");

        try {
            double newValue = parseDouble(newInput);
            if(isNaN(newValue) || isInfinite(newValue)) throw new CalculatorInputException("The new value cannot be NaN or infinite!");

            this.currentInput = newInput;
            this.currentValue = newValue;
            this.frozenValue = null;
            this.notifyRegisteredListeners();
        } catch (NumberFormatException e) {
            newInput = this.positive ? newInput : "-" + newInput;
            throw new CalculatorInputException("The given value \"" + newInput + "\" cannot be parsed into a double!");
        }
    }

    @Override
    public boolean isActiveOperandSet() {
        return this.activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (!this.isActiveOperandSet()) throw new IllegalStateException("The active operand has not yet been set!");
        return this.activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        this.editable = false;
        notifyRegisteredListeners();
    }

    @Override
    public void clearActiveOperand() {
        this.activeOperand = null;
        notifyRegisteredListeners();
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return this.pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
        this.notifyRegisteredListeners();
    }

    /**
     * Freezes the given value.
     *
     * @param value the value to be frozen.
     */
    @Override
    public void freezeValue(String value) {
        this.frozenValue = Objects.requireNonNull(value, "The given value to be frozen cannot be null!");
    }

    /**
     * Determines whether the model has a value frozen within it.
     *
     * @return {@code true} if some value is frozen, {@code false} otherwise.
     */
    @Override
    public boolean hasFrozenValue(){
        return this.frozenValue != null;
    }

    /**
     * Notifies all listeners within the {@code listeners}, registered for receiving events from the current model.
     */
    private void notifyRegisteredListeners() {
        this.listeners.forEach(listener -> listener.valueChanged(this));
    }

    @Override
    public String toString() {
        String sign = this.positive ? "" : "-";
        return this.frozenValue != null ? frozenValue :
                this.currentInput.isBlank() || this.currentInput.equals("0") ? sign + "0" : sign + this.currentInput;
    }
}
