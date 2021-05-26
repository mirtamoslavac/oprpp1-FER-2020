package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * The {@code ConditionalExpression} class represents a single conditional expression within a query.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ConditionalExpression {

    /**
     * Field value/attribute getter for the subject of comparison.
     */
    private final IFieldValueGetter fieldValueGetter;

    /**
     * String to which the attribute is being compared.
     */
    private final String stringLiteral;

    /**
     * Comparison operator that compares the {@code fieldValueGetter} and {@code stringLiteral}.
     */
    private final IComparisonOperator comparisonOperator;

    /**
     * Creates a new {@code ConditionalExpression} instance.
     *
     * @param valueGetter field value getter.
     * @param stringLiteral comparison string.
     * @param comparisonOperator comparison operator.
     * @throws NullPointerException when the given {@code valueGetter}, {@code stringLiteral} or {@code comparisonOperator} is {@code null}.
     */
    public ConditionalExpression(IFieldValueGetter valueGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
        this.fieldValueGetter = Objects.requireNonNull(valueGetter, "The given field value getter cannot be null!");
        this.stringLiteral = Objects.requireNonNull(stringLiteral, "The given string literal cannot be null!");
        this.comparisonOperator = Objects.requireNonNull(comparisonOperator, "The given comparison operator cannot be null!");
    }

    /**
     * Fetches the field value/attribute getter of the current conditional expression.
     *
     * @return attribute that is the subject of comparison.
     */
    public IFieldValueGetter getFieldValueGetter() {
        return fieldValueGetter;
    }

    /**
     * Fetches the string literal of the current conditional expression.
     *
     * @return string to which the attribute is being compared.
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * Fetches the comparison operator within the current conditional expression.
     *
     * @return comparison operator.
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
