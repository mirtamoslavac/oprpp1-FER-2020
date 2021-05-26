package hr.fer.oprpp1.hw04.db;

import java.util.List;
import java.util.Objects;

/**
 * The {@code QueryFilter} class creates a filter with the given list of {@link ConditionalExpression} instances.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class QueryFilter implements IFilter {
    /**
     * List of conditional expressions within a certain query.
     */
    private final List<ConditionalExpression> conditionalExpressionsList;

    /**
     * Creates a new {@code QueryFilter} instance.
     *
     * @param conditionalExpressionsList list of conditional expressions for the current filter.
     * @throws NullPointerException when the given {@code conditionalExpressionsList} is {@code null}.
     */
    public QueryFilter(List<ConditionalExpression> conditionalExpressionsList) {
        this.conditionalExpressionsList = Objects.requireNonNull(conditionalExpressionsList, "The given list of conditional expressions within a query cannot be null!");
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code record} is {@code null}.
     */
    @Override
    public boolean accepts(StudentRecord record) {
        Objects.requireNonNull(record, "The given student record cannot be null!");

        for (ConditionalExpression expression: this.conditionalExpressionsList) {
            if (!expression.getComparisonOperator().satisfied(expression.getFieldValueGetter().get(record), expression.getStringLiteral())) {
                return false;
            }
        }
        return true;
    }
}
