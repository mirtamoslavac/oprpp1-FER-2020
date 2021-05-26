package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldValueGettersTest {

    private IFieldValueGetter fieldValueGetter;
    private final StudentRecord student1 = new StudentRecord("0123456789", "Anić", "Ana", 3);

    @Test
    void testGetFirstNameCorrect() {
        this.fieldValueGetter = FieldValueGetters.FIRST_NAME;

        assertEquals("Ana", this.fieldValueGetter.get(student1));
    }

    @Test
    void testGetFirstNameIncorrect() {
        this.fieldValueGetter = FieldValueGetters.FIRST_NAME;

        assertNotEquals("Pero", this.fieldValueGetter.get(student1));
    }

    @Test
    void testGetLastNameCorrect() {
        this.fieldValueGetter = FieldValueGetters.LAST_NAME;

        assertEquals("Anić", this.fieldValueGetter.get(student1));
    }

    @Test
    void testGetLastNameIncorrect() {
        this.fieldValueGetter = FieldValueGetters.FIRST_NAME;

        assertNotEquals("Perić", this.fieldValueGetter.get(student1));
    }

    @Test
    void testGetJMBAGCorrect() {
        this.fieldValueGetter = FieldValueGetters.JMBAG;

        assertEquals("0123456789", this.fieldValueGetter.get(student1));
    }

    @Test
    void testGetJMBAGIncorrect() {
        this.fieldValueGetter = FieldValueGetters.FIRST_NAME;

        assertNotEquals("9876543210", this.fieldValueGetter.get(student1));
    }

}
