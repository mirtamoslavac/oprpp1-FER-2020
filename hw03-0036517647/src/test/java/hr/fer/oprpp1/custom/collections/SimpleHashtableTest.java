package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleHashtableTest {

    SimpleHashtable<String,Integer> examMarks;
    SimpleHashtable<Collection<String>,Integer> emptyHashtable;

    @BeforeEach
    void setUp() {
        this.emptyHashtable = new SimpleHashtable<>();
        this.examMarks = new SimpleHashtable<>(2);
        this.examMarks.put("Ivana", 2);
        this.examMarks.put("Ante", 2);
        this.examMarks.put("Jasna", 2);
        this.examMarks.put("Kristina", 5);
        this.examMarks.put("Marta", null);
        this.examMarks.put("Ivana", 5);
    }

    @Test
    void testConstructorWithInvalidSlotNumberThrows() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<Object,ObjectStack<String>>(-1));
    }

    @Test
    void testPutNew() {
        int oldSize = this.examMarks.size();

        assertNull(this.examMarks.put("Helena", 4));

        assertNotEquals(oldSize, this.examMarks.size());
        assertEquals(oldSize + 1, this.examMarks.size());
    }

    @Test
    void testPutIllegalThrows() {
        assertThrows(NullPointerException.class, () -> this.examMarks.put(null, 4));
    }

    @Test
    void testPutExistingOverwrite() {
        int oldValue = this.examMarks.get("Kristina");
        int newValue = 3;
        int oldSize = this.examMarks.size();
        int stillOldValue = this.examMarks.put("Kristina", newValue);

        assertNotEquals(oldValue, this.examMarks.get("Kristina"));
        assertEquals(oldValue, stillOldValue);
        assertEquals(newValue, this.examMarks.get("Kristina"));
        assertEquals(oldSize, this.examMarks.size());
        assertNotEquals(oldSize + 1, this.examMarks.size());
    }

    @Test
    void testGetExisting() {
        assertEquals(2, this.examMarks.get("Ante"));
    }

    @Test
    void testGetNonExisting() {
        assertNull(this.examMarks.get("Antun"));
    }

    @Test
    void testGetNonExistingDifferentType() {
        assertNull(this.examMarks.get(123));
    }

    @Test
    void testGetNull() {
        assertNull(this.examMarks.get(null));
    }

    @Test
    void testContainsKeyTrue() {
        assertTrue(this.examMarks.containsKey("Marta"));
    }

    @Test
    void testContainsKeyFalse() {
        assertFalse(this.examMarks.containsKey("Mirta"));
    }

    @Test
    void testContainsValueTrue() {
        assertTrue(this.examMarks.containsValue(5));
    }

    @Test
    void testContainsValueNullTrue() {
        assertTrue(this.examMarks.containsValue(null));
    }

    @Test
    void testContainsValueFalse() {
        assertFalse(this.examMarks.containsValue(3));
    }

    @Test
    void testSizeEmptyHashtable() {
        assertEquals(0, this.emptyHashtable.size());
    }

    @Test
    void testSizeNonEmptyHashtable() {
        assertEquals(5, this.examMarks.size());
    }

    @Test
    void testRemoveExisting() {
        assertNotNull(this.examMarks.get("Ante"));
        int oldSize = this.examMarks.size();

        this.examMarks.remove("Ante");

        assertNull(this.examMarks.get("Ante"));
        assertNotEquals(oldSize, this.examMarks.size());
        assertEquals(oldSize - 1, this.examMarks.size());
    }

    @Test
    void testRemoveNonExisting() {
        assertNull(this.examMarks.get("Ivica"));
        int oldSize = this.examMarks.size();

        assertNull(this.examMarks.remove("Ivica"));
        assertEquals(oldSize, this.examMarks.size());
        assertNotEquals(oldSize - 1, this.examMarks.size());
    }

    @Test
    void testRemoveDifferentType() {
        assertNull(this.examMarks.get(12));
        int oldSize = this.examMarks.size();

        assertNull(this.examMarks.remove(12));
        assertEquals(oldSize, this.examMarks.size());
        assertNotEquals(oldSize - 1, this.examMarks.size());
    }

    @Test
    void testRemoveNull() {
        assertNull(this.examMarks.remove(null));
    }

    @Test
    void testIsEmptyTrue() {
        assertTrue(this.emptyHashtable.isEmpty());
    }

    @Test
    void testIsEmptyFalse() {
        assertFalse(this.examMarks.isEmpty());
    }

    @Test
    void testEmptyHashtableToArray() {
        SimpleHashtable.TableEntry<Collection<String>,Integer> [] array = this.emptyHashtable.toArray();
        assertEquals(0, array.length);
        for (SimpleHashtable.TableEntry<Collection<String>,Integer> element : array) {
            assertNull(element);
        }
    }

    @Test
    void testNonEmptyHashtableToArray() {
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();
        assertEquals(this.examMarks.size(), array.length);
        for (SimpleHashtable.TableEntry<String,Integer> element : array) {
            assertNotNull(element);
        }
    }

    @Test
    void testClearEmptyHashtable() {
        SimpleHashtable.TableEntry<Collection<String>,Integer> [] array = this.emptyHashtable.toArray();
        assertEquals(0, array.length);
        for (SimpleHashtable.TableEntry<Collection<String>,Integer> element : array) {
            assertNull(element);
        }

        this.examMarks.clear();

        array = this.emptyHashtable.toArray();
        assertEquals(0, array.length);
        for (SimpleHashtable.TableEntry<Collection<String>,Integer> element : array) {
            assertNull(element);
        }
    }

    @Test
    void testClearNonEmptyHashtable() {
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();
        assertEquals(this.examMarks.size(), array.length);
        for (SimpleHashtable.TableEntry<String,Integer> element : array) {
            assertNotNull(element);
        }

        this.examMarks.clear();

        array = this.examMarks.toArray();
        assertEquals(0, array.length);
        assertEquals(this.examMarks.size(), array.length);
        for (SimpleHashtable.TableEntry<String,Integer> element : array) {
            assertNull(element);
        }

        assertNotEquals(new SimpleHashtable<>(8), this.examMarks); //not equal because of residual modificationCount
    }

    @Test
    void testIterator() {
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = this.examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();

        int numberOfEntriesToGoThrough = examMarks.size();
        int numberOfEntriesGoneThrough = 0;

        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            assertEquals(array[numberOfEntriesGoneThrough].getKey(), pair.getKey());
            assertEquals(array[numberOfEntriesGoneThrough].getValue(), pair.getValue());
            iter.remove();
            numberOfEntriesGoneThrough++;
        }

        assertNotEquals(numberOfEntriesToGoThrough, this.examMarks.size());
        assertEquals(0, this.examMarks.size());
        assertEquals(numberOfEntriesGoneThrough, numberOfEntriesToGoThrough);
    }

    @Test
    void testIteratorHasNextWhenNewEntryAddedThrows() {
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = this.examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();

        int numberOfEntriesGoneThrough = 0;

        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            assertEquals(array[numberOfEntriesGoneThrough].getKey(), pair.getKey());
            assertEquals(array[numberOfEntriesGoneThrough].getValue(), pair.getValue());
            iter.remove();
            numberOfEntriesGoneThrough++;
        }

        this.examMarks.put("Kiara", 3);

        assertThrows(ConcurrentModificationException.class, iter::hasNext);
    }

    @Test
    void testIteratorRemoveEntryWhenAlreadyModifiedThrows() {
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = this.examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();

        SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
        assertEquals(array[0].getKey(), pair.getKey());
        assertEquals(array[0].getValue(), pair.getValue());
        this.examMarks.put("Kiara", 3);
        assertThrows(ConcurrentModificationException.class, iter::remove);

    }

    @Test
    void testIteratorRemoveEntryWhenAlreadyRemovedThrows() {
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = this.examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();

        SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
        assertEquals(array[0].getKey(), pair.getKey());
        assertEquals(array[0].getValue(), pair.getValue());
        iter.remove();
        assertThrows(IllegalStateException.class, iter::remove);
    }

    @Test
    void testIteratorNextWhenEmptyThrows() {
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = this.examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> [] array = this.examMarks.toArray();

        int numberOfEntriesGoneThrough = 0;

        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            assertEquals(array[numberOfEntriesGoneThrough].getKey(), pair.getKey());
            assertEquals(array[numberOfEntriesGoneThrough].getValue(), pair.getValue());
            iter.remove();
            numberOfEntriesGoneThrough++;
        }

        assertThrows(NoSuchElementException.class, iter::next);
    }

}
