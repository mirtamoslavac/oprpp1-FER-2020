package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryFilterTest {
    private final List<ConditionalExpression> conditionalExpressionsListSingle = new ArrayList<>();
    private final List<ConditionalExpression> conditionalExpressionsListMultiple = new ArrayList<>();

    private QueryFilter queryFilter;

    @BeforeEach
    void setUp() {
        this.conditionalExpressionsListSingle.add(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000009", ComparisonOperators.LESS));

        this.conditionalExpressionsListMultiple.add(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000009", ComparisonOperators.LESS));
        this.conditionalExpressionsListMultiple.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "*ić", ComparisonOperators.LIKE));
        this.conditionalExpressionsListMultiple.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "3", ComparisonOperators.GREATER_OR_EQUALS));
    }

    @Test
    void testSingleCondition() {
        this.queryFilter = new QueryFilter(this.conditionalExpressionsListSingle);

        assertTrue(queryFilter.accepts(new StudentRecord("0000000001", "Anić", "Ana", 5)));
        assertTrue(queryFilter.accepts(new StudentRecord("0000000008", "Naliv", "Pero", 4)));
        assertFalse(queryFilter.accepts(new StudentRecord("0000000009", "Nerić", "Nera", 3)));
        assertFalse(queryFilter.accepts(new StudentRecord("0000000010", "Pen", "Kala", 2)));
    }

    @Test
    void testMultipleConditions() {
        this.queryFilter = new QueryFilter(this.conditionalExpressionsListMultiple);

        assertTrue(queryFilter.accepts(new StudentRecord("0000000001", "Anić", "Ana", 5)));
        assertFalse(queryFilter.accepts(new StudentRecord("0000000008", "Naliv", "Pero", 4)));
        assertFalse(queryFilter.accepts(new StudentRecord("0000000009", "Nerić", "Nera", 3)));
        assertFalse(queryFilter.accepts(new StudentRecord("0000000010", "Pen", "Kala", 2)));
    }

    @Test
    void testPassNullConditionalExpressionsListThrows() {
        assertThrows(NullPointerException.class, () -> new QueryFilter(null));
    }
}
