package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleHashtableTest {

    SimpleHashtable<String,Integer> examMarks;
    SimpleHashtable<Collection<String>,Integer> emptyHashtable;

    @BeforeEach
    void setUp() {
        emptyHashtable = new SimpleHashtable<>();
        examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Marta", null);
        examMarks.put("Ivana", 5);
    }

    @Test
    void testConstructorWithInvalidSlotNumberThrows() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<Object,ObjectStack<String>>(-1));
    }

    @Test
    void testPutNew() {
        int oldSize = examMarks.size();

        assertNull(examMarks.put("Helena", 4));

        assertNotEquals(oldSize, examMarks.size());
        assertEquals(oldSize + 1, examMarks.size());
    }

    @Test
    void testPutIllegal() {
        assertThrows(NullPointerException.class, () -> examMarks.put(null, 4));
    }

    @Test
    void testPutExistingOverwrite() {
        int oldValue = examMarks.get("Kristina");
        int newValue = 3;
        int oldSize = examMarks.size();
        int stillOldValue = examMarks.put("Kristina", newValue);

        assertNotEquals(oldValue, examMarks.get("Kristina"));
        assertEquals(oldValue, stillOldValue);
        assertEquals(newValue, examMarks.get("Kristina"));
        assertEquals(oldSize, examMarks.size());
        assertNotEquals(oldSize + 1, examMarks.size());
    }

    @Test
    void testGetExisting() {
        assertEquals(2, examMarks.get("Ante"));
    }

    @Test
    void testGetNonExisting() {
        assertNull(examMarks.get("Antun"));
    }

    @Test
    void testGetNonExistingDifferentType() {
        assertNull(examMarks.get(123));
    }

    @Test
    void testGetNull() {
        assertNull(examMarks.get(null));
    }

    @Test
    void testContainsKeyTrue() {
        assertTrue(examMarks.containsKey("Marta"));
    }

    @Test
    void testContainsKeyFalse() {
        assertFalse(examMarks.containsKey("Mirta"));
    }

    @Test
    void testContainsValueTrue() {
        assertTrue(examMarks.containsValue(5));
    }

    @Test
    void testContainsValueNullTrue() {
        assertTrue(examMarks.containsValue(null));
    }

    @Test
    void testContainsValueFalse() {
        assertFalse(examMarks.containsValue(3));
    }

    @Test
    void testSizeEmptyHashtable() {
        assertEquals(0, emptyHashtable.size());
    }

    @Test
    void testSizeNonEmptyHashtable() {
        assertEquals(5, examMarks.size());
    }

    @Test
    void testRemoveExisting() {
        assertNotNull(examMarks.get("Ante"));
        int oldSize = examMarks.size();

        examMarks.remove("Ante");

        assertNull(examMarks.get("Ante"));
        assertNotEquals(oldSize, examMarks.size());
        assertEquals(oldSize - 1, examMarks.size());
    }

    @Test
    void testRemoveNonExisting() {
        assertNull(examMarks.get("Ivica"));
        int oldSize = examMarks.size();

        assertNull(examMarks.remove("Ivica"));
        assertEquals(oldSize, examMarks.size());
        assertNotEquals(oldSize - 1, examMarks.size());
    }

    @Test
    void testRemoveDifferentType() {
        assertNull(examMarks.get(12));
        int oldSize = examMarks.size();

        assertNull(examMarks.remove(12));
        assertEquals(oldSize, examMarks.size());
        assertNotEquals(oldSize - 1, examMarks.size());
    }

    @Test
    void testRemoveNull() {
        assertNull(examMarks.remove(null));
    }

    @Test
    void testIsEmptyTrue() {
        assertTrue(emptyHashtable.isEmpty());
    }

    @Test
    void testIsEmptyFalse() {
        assertFalse(examMarks.isEmpty());
    }

    @Test
    void testEmptyHashtableToArray() {
        SimpleHashtable.TableEntry<Collection<String>,Integer> [] array = emptyHashtable.toArray();
        assertEquals(0, array.length);
        for (SimpleHashtable.TableEntry<Collection<String>,Integer> element : array) {
            assertNull(element);
        }
    }

    @Test
    void testNonEmptyHashtableToArray() {
        SimpleHashtable.TableEntry<String,Integer> [] array = examMarks.toArray();
        assertEquals(examMarks.size(), array.length);
        for (SimpleHashtable.TableEntry<String,Integer> element : array) {
            assertNotNull(element);
        }
    }

    @Test
    void testClearEmptyHashtable() {
        SimpleHashtable.TableEntry<Collection<String>,Integer> [] array = emptyHashtable.toArray();
        assertEquals(0, array.length);
        for (SimpleHashtable.TableEntry<Collection<String>,Integer> element : array) {
            assertNull(element);
        }

        examMarks.clear();

        array = emptyHashtable.toArray();
        assertEquals(0, array.length);
        for (SimpleHashtable.TableEntry<Collection<String>,Integer> element : array) {
            assertNull(element);
        }
    }

    @Test
    void testClearNonEmptyHashtable() {
        SimpleHashtable.TableEntry<String,Integer> [] array = examMarks.toArray();
        assertEquals(examMarks.size(), array.length);
        for (SimpleHashtable.TableEntry<String,Integer> element : array) {
            assertNotNull(element);
        }

        examMarks.clear();

        array = examMarks.toArray();
        assertEquals(0, array.length);
        assertEquals(examMarks.size(), array.length);
        for (SimpleHashtable.TableEntry<String,Integer> element : array) {
            assertNull(element);
        }

        assertNotEquals(new SimpleHashtable<>(8), examMarks); //not equal because of residual modificationCount
    }

    @Test
    void testIterator() {
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> [] array = examMarks.toArray();

        int numberOfEntriesToGoThrough = examMarks.size();
        int numberOfEntriesGoneThrough = 0;

        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            assertEquals(array[numberOfEntriesGoneThrough].getKey(), pair.getKey());
            assertEquals(array[numberOfEntriesGoneThrough].getValue(), pair.getValue());
            iter.remove();
            numberOfEntriesGoneThrough++;
        }

        assertNotEquals(numberOfEntriesToGoThrough, examMarks.size());
        assertEquals(0, examMarks.size());
        assertEquals(numberOfEntriesGoneThrough, numberOfEntriesToGoThrough);
    }

}
