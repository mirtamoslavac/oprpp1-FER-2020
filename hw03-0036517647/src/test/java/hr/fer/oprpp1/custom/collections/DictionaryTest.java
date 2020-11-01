package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.math.Vector2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {

    private final Random rand = new Random();

    private Dictionary<Object,Object> emptyDictionary;
    private Dictionary<Object,Object> dictionaryContainingEntriesObjects;
    private Dictionary<Integer,String> dictionaryContainingEntriesIntString;

    @BeforeEach
    void setUp() {
        emptyDictionary = new Dictionary<>();

        dictionaryContainingEntriesObjects = new Dictionary<>();
        dictionaryContainingEntriesObjects.put(1.5, rand.nextDouble() * 1000);
        dictionaryContainingEntriesObjects.put(-90, rand.nextInt(1000));
        dictionaryContainingEntriesObjects.put("Key", rand.nextBoolean());
        dictionaryContainingEntriesObjects.put('+', "testing testing!");
        dictionaryContainingEntriesObjects.put(new Vector2D(1, 1), new ArrayIndexedCollection<>());

        dictionaryContainingEntriesIntString = new Dictionary<>();
        dictionaryContainingEntriesIntString.put(1, "A");
        dictionaryContainingEntriesIntString.put(2, "B");
        dictionaryContainingEntriesIntString.put(3, "C");
       }

    @Test
    void testDefaultConstructor() {
        Dictionary<Object, Object> testingDictionary = new Dictionary<>();

        assertEquals(0, testingDictionary.size());
    }

    @Test
    void testIsEmptyTrue() {
        assertTrue(emptyDictionary.isEmpty());
        assertEquals(0, emptyDictionary.size());
    }

    @Test
    void testIsEmptyFalse() {
        assertFalse(dictionaryContainingEntriesObjects.isEmpty());
        assertNotEquals(0, dictionaryContainingEntriesObjects.size());
    }

    @Test
    void testSize() {
        assertEquals(0, emptyDictionary.size());
        assertEquals(5, dictionaryContainingEntriesObjects.size());
        assertEquals(3, dictionaryContainingEntriesIntString.size());
    }

    @Test
    void testClearDictionaryThatIsEmpty() {
        assertTrue(emptyDictionary.isEmpty());
        assertEquals(0, emptyDictionary.size());

        emptyDictionary.clear();
        assertTrue(emptyDictionary.isEmpty());
        assertEquals(0, emptyDictionary.size());
    }

    @Test
    void testPutCompletelyNewEntry() {
        assertEquals(3, dictionaryContainingEntriesIntString.size());

        dictionaryContainingEntriesIntString.put(4, "D");

        assertEquals(4, dictionaryContainingEntriesIntString.size());
    }

    @Test
    void testPutEntryWithAlreadyExistingKey() {
        assertEquals(3, dictionaryContainingEntriesIntString.size());
        assertEquals("B", dictionaryContainingEntriesIntString.get(2));

        dictionaryContainingEntriesIntString.put(2, "bla bla");

        assertNotEquals("B", dictionaryContainingEntriesIntString.get(2));
        assertEquals("bla bla", dictionaryContainingEntriesIntString.get(2));
        assertEquals(3, dictionaryContainingEntriesIntString.size());
    }

    @Test
    void testPutNullKeyThrows() {
        assertThrows(NullPointerException.class, () -> dictionaryContainingEntriesObjects.put(null, 1));
    }

    @Test
    void testGetExistingEntry() {
        assertEquals("B", dictionaryContainingEntriesIntString.get(2));
    }

    @Test
    void testGetEntryThatDoesNotExist() {
        assertNull(dictionaryContainingEntriesIntString.get(4));
    }

    @Test
    void testGetEntryOutOfBounds() {
        assertNull(dictionaryContainingEntriesIntString.get(-4));
    }

    @Test
    void testGetNullKeyThrows() {
        assertThrows(NullPointerException.class, () -> dictionaryContainingEntriesObjects.get(null));
    }

    @Test
    void testGetDifferentType() {
        assertNull(dictionaryContainingEntriesIntString.get("first"));
    }

    @Test
    void testRemoveExistingEntry() {
        assertEquals("B", dictionaryContainingEntriesIntString.remove(2));
        assertNull(dictionaryContainingEntriesIntString.get(2));
    }

    @Test
    void testRemoveEntryThatDoesNotExist() {
        assertNull(dictionaryContainingEntriesIntString.remove(4));
    }

    @Test
    void testRemoveNullKeyThrows() {
        assertThrows(NullPointerException.class, () -> dictionaryContainingEntriesObjects.remove(null));
    }

    @Test
    void testEqualsTrue() {
        assertEquals(emptyDictionary, new Dictionary<>());
    }

    @Test
    void testEqualsFalse() {
        assertNotEquals(emptyDictionary, dictionaryContainingEntriesObjects);
    }
}
