package hr.fer.oprpp1.hw04.db;

/**
 * The {@code IComparisonOperator} interface represents a strategy responsible for determining whether the two given values are in compliance with a certain comparison operation.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
@FunctionalInterface
public interface IComparisonOperator {

    /**
     * Determines whether the two given string literals (not field names) satisfy a certain comparison operation.
     *
     * @param value1 the value that is being compared.
     * @param value2 the value that is the comparison.
     * @return {@code true} if the values satisfy the wanted comparison, {@code false} otherwise.
     */
    boolean satisfied(String value1, String value2);
}
