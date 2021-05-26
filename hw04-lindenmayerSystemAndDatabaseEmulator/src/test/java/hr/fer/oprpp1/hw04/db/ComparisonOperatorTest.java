package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorTest {

    private IComparisonOperator comparisonOperator;

    @Test
    void testLessCorrect() {
        this.comparisonOperator = ComparisonOperators.LESS;

        assertTrue(this.comparisonOperator.satisfied("1", "2"));
    }

    @Test
    void testLessIncorrect() {
        this.comparisonOperator = ComparisonOperators.LESS;

        assertFalse(this.comparisonOperator.satisfied("2", "2"));
        assertFalse(this.comparisonOperator.satisfied("3", "2"));
    }

    @Test
    void testLessOrEqualsCorrect() {
        this.comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;

        assertTrue(this.comparisonOperator.satisfied("1", "2"));
        assertTrue(this.comparisonOperator.satisfied("2", "2"));
    }

    @Test
    void testLessOrEqualsIncorrect() {
        this.comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;

        assertFalse(this.comparisonOperator.satisfied("3", "2"));
    }

    @Test
    void testGreaterCorrect() {
        this.comparisonOperator = ComparisonOperators.GREATER;

        assertTrue(this.comparisonOperator.satisfied("2", "1"));
    }

    @Test
    void testGreaterIncorrect() {
        this.comparisonOperator = ComparisonOperators.GREATER;

        assertFalse(this.comparisonOperator.satisfied("2", "2"));
        assertFalse(this.comparisonOperator.satisfied("1", "2"));
    }

    @Test
    void testGreaterOrEqualsCorrect() {
        this.comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;

        assertTrue(this.comparisonOperator.satisfied("2", "1"));
        assertTrue(this.comparisonOperator.satisfied("2", "2"));
    }

    @Test
    void testGreaterOrEqualsIncorrect() {
        this.comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;

        assertFalse(this.comparisonOperator.satisfied("1", "2"));
    }

    @Test
    void testEqualsCorrect() {
        this.comparisonOperator = ComparisonOperators.EQUALS;

        assertTrue(this.comparisonOperator.satisfied("2", "2"));
    }

    @Test
    void testEqualsIncorrect() {
        this.comparisonOperator = ComparisonOperators.EQUALS;

        assertFalse(this.comparisonOperator.satisfied("1", "2"));
        assertFalse(this.comparisonOperator.satisfied("2", "1"));
    }

    @Test
    void testNotEqualsCorrect() {
        this.comparisonOperator = ComparisonOperators.NOT_EQUALS;

        assertTrue(this.comparisonOperator.satisfied("1", "2"));
        assertTrue(this.comparisonOperator.satisfied("2", "1"));
    }

    @Test
    void testNotEqualsIncorrect() {
        this.comparisonOperator = ComparisonOperators.NOT_EQUALS;

        assertFalse(this.comparisonOperator.satisfied("2", "2"));
    }

    @Test
    void testLikeCorrect() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("AAA", "AAA"));
    }

    @Test
    void testLikeCorrectWildcardSingleBeginning() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("AAAA", "*AAA"));
        assertTrue(this.comparisonOperator.satisfied("AAAA", "*AAAA"));
        assertTrue(this.comparisonOperator.satisfied("AA7AA", "*A7AA"));
        assertTrue(this.comparisonOperator.satisfied("AA7AA", "*AA7AA"));
    }

    @Test
    void testLikeCorrectWildcardSingleMiddle() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("AAAA", "AA*AA"));
        assertTrue(this.comparisonOperator.satisfied("AAAAA", "AA*AA"));
        assertTrue(this.comparisonOperator.satisfied("AAaAA", "AA*AA"));
        assertTrue(this.comparisonOperator.satisfied("AA7AA", "AA*AA"));
    }

    @Test
    void testLikeCorrectWildcardSingleEnding() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("AAAA", "AAA*"));
        assertTrue(this.comparisonOperator.satisfied("AAAA", "AAAA*"));
        assertTrue(this.comparisonOperator.satisfied("AA7A", "AA7A*"));
        assertTrue(this.comparisonOperator.satisfied("AA7AA", "AA7A*"));
    }

    @Test
    void testLikeCorrectWildcardMultipleBeginning() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("Zagreb", "*greb"));
    }

    @Test
    void testLikeCorrectWildcardMultipleMiddle() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("Zagreb", "Z*b"));
    }

    @Test
    void testLikeCorrectWildcardMultipleEnding() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertTrue(this.comparisonOperator.satisfied("Zagreb", "Zagr*"));
    }

    @Test
    void testLikeIncorrect() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertFalse(this.comparisonOperator.satisfied("AAaA", "AAA"));
    }

    @Test
    void testLikeIncorrectWildcard() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertFalse(this.comparisonOperator.satisfied("ABBA", "A*AA"));
        assertFalse(this.comparisonOperator.satisfied("123", "13*"));
    }

    @Test
    void testLikeMultipleWildcardsThrows() {
        this.comparisonOperator = ComparisonOperators.LIKE;

        assertThrows(IllegalArgumentException.class, () -> this.comparisonOperator.satisfied("Zagreb", "*agr*b"));
    }
}
