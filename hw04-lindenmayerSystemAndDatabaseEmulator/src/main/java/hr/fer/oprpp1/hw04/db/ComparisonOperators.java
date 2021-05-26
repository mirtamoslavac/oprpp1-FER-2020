package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * The {@code ComparisonOperators} class defines comparison operators, {@link IComparisonOperator} implementations, valid in queries over the database.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ComparisonOperators {
    /**
     * Character representing a wildcard in the LIKE query
     */
    private static final char WILDCARD_ELEMENT = '*';

    /**
     * LESS operator implementation.
     */
    public static final IComparisonOperator LESS = ((value1, value2) -> value1.compareTo(value2) < 0);

    /**
     * LESS OR EQUALS operator implementation.
     */
    public static final IComparisonOperator LESS_OR_EQUALS = ((value1, value2) -> value1.compareTo(value2) <= 0);

    /**
     * GREATER operator implementation.
     */
    public static final IComparisonOperator GREATER = ((value1, value2) -> value1.compareTo(value2) > 0);

    /**
     * GREATER OR EQUALS operator implementation.
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = ((value1, value2) -> value1.compareTo(value2) >= 0);

    /**
     * EQUALS operator implementation.
     */
    public static final IComparisonOperator EQUALS = ((value1, value2) -> value1.compareTo(value2) == 0);

    /**
     * NOT EQUALS operator implementation.
     */
    public static final IComparisonOperator NOT_EQUALS = ((value1, value2) -> value1.compareTo(value2) != 0);

    /**
     * LIKE operator implementation, with the first value being the string that is to be checked, and the second value the pattern to be checked.
     */
    public static final IComparisonOperator LIKE = ((value1, value2) -> {
        Objects.requireNonNull(value1, "The value1 cannot be null!");
        Objects.requireNonNull(value2, "The value2 cannot be null!");
        int wildcardCounter = 0;

        for (int i = 0, value2Length = value2.length(); i < value2Length; i++) {
            if (value2.charAt(i) == WILDCARD_ELEMENT) {
                wildcardCounter++;
                if (wildcardCounter > 1) throw new IllegalArgumentException("More than one wildcard character entered!");
            }
        }
        return value1.matches(value2.replace(String.valueOf(WILDCARD_ELEMENT), ".*"));
    });
}
