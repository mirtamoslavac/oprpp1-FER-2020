package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionalExpressionTest {

    private StudentRecord record;

    private final ConditionalExpression expr = new ConditionalExpression(
            FieldValueGetters.LAST_NAME,
            "Bos*",
            ComparisonOperators.LIKE);

    @Test
    void testConditionalExpressionConstructorAndGetters() {
        ConditionalExpression testedExpr = new ConditionalExpression(
                FieldValueGetters.FIRST_NAME,
                "Maja",
                ComparisonOperators.LESS_OR_EQUALS);

        assertEquals(FieldValueGetters.FIRST_NAME, testedExpr.getFieldValueGetter());
        assertEquals("Maja", testedExpr.getStringLiteral());
        assertEquals(ComparisonOperators.LESS_OR_EQUALS, testedExpr.getComparisonOperator());
    }

    @Test
    void testConditionalExpressionSatisfies() {
        this.record = new StudentRecord("0123456789", "Boss", "The", 5);

        assertTrue(this.expr.getComparisonOperator().satisfied(
                this.expr.getFieldValueGetter().get(record),
                this.expr.getStringLiteral()));
    }

    @Test
    void testConditionalExpressionDoesNotSatisfy() {
        this.record = new StudentRecord("0123456789", "Bass", "The", 5);

        assertFalse(this.expr.getComparisonOperator().satisfied(
                this.expr.getFieldValueGetter().get(record),
                this.expr.getStringLiteral()));
    }

    @Test
    void testNullValuesWhileComparingWithLIKEThrows() {
        assertThrows(NullPointerException.class, () -> this.expr.getComparisonOperator().satisfied(null, "123"));

        assertThrows(NullPointerException.class, () -> this.expr.getComparisonOperator().satisfied("ab", null));

        assertThrows(NullPointerException.class, () -> this.expr.getComparisonOperator().satisfied(null, null));
    }

}
