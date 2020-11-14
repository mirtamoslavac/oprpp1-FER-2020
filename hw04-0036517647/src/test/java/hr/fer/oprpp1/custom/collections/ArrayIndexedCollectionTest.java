package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class ArrayIndexedCollectionTest {

    private final Random rand = new Random();

    private ArrayIndexedCollection<Object> emptyCollection;
    private ArrayIndexedCollection<Object> twoElementCollection;
    private ArrayIndexedCollection<Object> fiveElementCollection;
    private ArrayIndexedCollection<Object> fullCollection;
    private ArrayIndexedCollection<Object> persistentCollection;

    @BeforeEach
    void setUp() {
        emptyCollection = new ArrayIndexedCollection<>();
        twoElementCollection = new ArrayIndexedCollection<>();
        for (int i = 0; i < 2; i++) {
            int randNum = rand.nextInt(5);
            switch (randNum) {
                case 0 -> twoElementCollection.add(rand.nextDouble()*1000);
                case 1 -> twoElementCollection.add(rand.nextInt(1000));
                case 2 -> twoElementCollection.add(rand.nextBoolean());
                case 3 -> twoElementCollection.add("testing testing!");
                case 4 -> twoElementCollection.add(new ArrayIndexedCollection<>());
            }
        }
        fiveElementCollection = new ArrayIndexedCollection<>();
        for (int i = 0; i < 5; i++) {
            int randNum = rand.nextInt(5);
            switch (randNum) {
                case 0 -> fiveElementCollection.add(rand.nextDouble()*1000);
                case 1 -> fiveElementCollection.add(rand.nextInt(1000));
                case 2 -> fiveElementCollection.add(rand.nextBoolean());
                case 3 -> fiveElementCollection.add("testing testing!");
                case 4 -> fiveElementCollection.add(new ArrayIndexedCollection<>());
            }
        }
        fullCollection = new ArrayIndexedCollection<>(7);
        for (int i = 0; i < 7; i++) {
            int randNum = rand.nextInt(5);
            switch (randNum) {
                case 0 -> fullCollection.add(rand.nextDouble()*1000);
                case 1 -> fullCollection.add(rand.nextInt(1000));
                case 2 -> fullCollection.add(rand.nextBoolean());
                case 3 -> fullCollection.add("testing testing!");
                case 4 -> fullCollection.add(new ArrayIndexedCollection<>());
            }
        }

        persistentCollection = new ArrayIndexedCollection<>(3);
        persistentCollection.add(123);
        persistentCollection.add(456);
        persistentCollection.add(789);
    }

    @Test
    public void testNumberOfContainingElementsInEmptyCollectionConstructor1() {
        assertEquals(0, new ArrayIndexedCollection<>().size());
    }

    @Test
    public void testNumberOfContainingElementsInEmptyCollectionConstructor2() {
        assertEquals(0, new ArrayIndexedCollection<>(7).size());
    }

    @Test
    public void testNumberOfContainingElementsInEmptyCollectionConstructor3() {
        assertEquals(0, new ArrayIndexedCollection<>(emptyCollection).size());
    }

    @Test
    public void testNumberOfContainingElementsInEmptyCollectionConstructor4() {
        assertEquals(0, new ArrayIndexedCollection<>(emptyCollection, 7).size());
    }

    @Test
    public void testNumberOfContainingElementsInNonEmptyCollectionConstructor3() {
        assertEquals(5, new ArrayIndexedCollection<>(fiveElementCollection).size());
    }

    @Test
    public void testNumberOfContainingElementsInNonEmptyCollectionConstructor4() {
        assertEquals(5, new ArrayIndexedCollection<>(fiveElementCollection, 7).size());
    }


    @Test
    public void testIncorrectInitialCapacityShouldThrowConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(-1));
    }

    @Test
    public void testIncorrectInitialCapacityShouldThrowConstructor4() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(fiveElementCollection, 0));
    }

    @Test
    public void testPassNullAsOtherCollectionShouldThrowConstructor2() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null));
    }

    @Test
    public void testPassNullAsOtherCollectionShouldThrowConstructor4() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null, 7));
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null, 0));
    }

    @Test
    public void testAddAllFromOneCollectionToOtherCollectionAlreadyContainingElements() {
        int expectedSize = twoElementCollection.size() + fiveElementCollection.size();
        fiveElementCollection.addAll(twoElementCollection);
        assertEquals(expectedSize, fiveElementCollection.size());
    }

    @Test
    public void testAddAllFromOneCollectionToOtherFullCollection() {
        int expectedSize = fullCollection.size() + twoElementCollection.size();
        fullCollection.addAll(twoElementCollection);
        assertEquals(expectedSize, fullCollection.size());
    }

    @Test
    public void testAddAllFromOneCollectionToOtherEmptyCollection() {
        int expectedSize = emptyCollection.size() + twoElementCollection.size();
        emptyCollection.addAll(twoElementCollection);
        assertEquals(expectedSize, emptyCollection.size());
    }

    @Test
    public void testAddAllFromEmptyCollectionToOtherCollection() {
        int expectedSize = twoElementCollection.size() + emptyCollection.size();
        twoElementCollection.addAll(emptyCollection);
        assertEquals(expectedSize, twoElementCollection.size());

        expectedSize = emptyCollection.size() + emptyCollection.size();
        emptyCollection.addAll(emptyCollection);
        assertEquals(expectedSize, emptyCollection.size());
    }

    @Test
    public void testSizeOfEmptyCollection() {
        assertEquals(0, emptyCollection.size());
    }

    @Test
    public void testSizeOfCollectionsContainingElements() {
        assertEquals(2, twoElementCollection.size());
        assertEquals(5, fiveElementCollection.size());
        assertEquals(7, fullCollection.size());
    }

    @Test
    public void testIsEmptyCollectionEmpty() {
        assertTrue(emptyCollection.isEmpty());
    }

    @Test
    public void testIsCollectionContainingElementsEmpty() {
        assertFalse(fullCollection.isEmpty());
    }

    @Test
    public void testEmptyCollectionToArray() {
        assertNotNull(emptyCollection.toArray());
        assertArrayEquals(new Object[0], emptyCollection.toArray());
    }

    @Test
    public void testCollectionContainingElementsToArray() {
        assertArrayEquals(new Object[]{123, 456, 789}, persistentCollection.toArray());
    }

    @Test
    public void testAddToEmptyCollection() {
        int expectedSize = emptyCollection.size();
        emptyCollection.add("add one element");
        assertEquals(expectedSize + 1, emptyCollection.size());
    }

    @Test
    public void testAddToCollectionContainingElements() {
        int expectedSize = fiveElementCollection.size();
        fiveElementCollection.add("add one element");
        assertEquals(expectedSize + 1, fiveElementCollection.size());
    }

    @Test
    public void testAddToFullCollection() {
        int expectedSize = fullCollection.size();
        fullCollection.add("add one element");
        assertEquals(expectedSize + 1, fullCollection.size());
    }

    @Test
    public void testPassNullAsElementToAddShouldThrow() {
        assertThrows(NullPointerException.class, () -> emptyCollection.add(null));
        assertThrows(NullPointerException.class, () -> twoElementCollection.add(null));
    }

    @Test
    public void testForEach(){
        Object[] resultArray = new Object[5];

        class TestingProcessor implements Processor<Object> {
            @Override
            public void process(Object value) {
                    int valueInt = (int)value;
                    resultArray[valueInt-1] = valueInt - 2;
            }
        }

        ArrayIndexedCollection<Object> testForEachCollection = new ArrayIndexedCollection<>(5);
        testForEachCollection.add(1);
        testForEachCollection.add(2);
        testForEachCollection.add(3);
        testForEachCollection.add(4);
        testForEachCollection.add(5);

        TestingProcessor processor = new TestingProcessor();
        testForEachCollection.forEach(processor);

        assertArrayEquals(new Object[]{-1, 0, 1, 2, 3}, resultArray);
    }

    @Test
    public void testClearEmptyCollection() {
        emptyCollection.clear();
        assertEquals(new ArrayIndexedCollection<>(), emptyCollection);
    }

    @Test
    public void testClearCollectionContainingElements() {
        twoElementCollection.clear();
        assertArrayEquals(new Object[twoElementCollection.toArray().length], twoElementCollection.toArray());
    }

    @Test
    public void testGetFromIndex() {
        assertEquals(persistentCollection.get(2), persistentCollection.toArray()[2]);
        assertNotEquals(persistentCollection.get(1), persistentCollection.toArray()[2]);
        assertNotEquals(persistentCollection.get(0), persistentCollection.toArray()[1]);
    }

    @Test
    public void testGetFromIndexOutOfRangeShouldThrow() {
        assertThrows(IndexOutOfBoundsException.class, () -> persistentCollection.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> persistentCollection.get(10));
    }

    @Test
    public void testInsertAtBeginningOfCollection() {
        Object[] storedArray = persistentCollection.toArray();
        int expectedSize = persistentCollection.size() + 1;
        persistentCollection.insert("going in", 0);
        Object[]newArray = persistentCollection.toArray();
        assertNotEquals(storedArray, newArray);
        assertNotEquals(storedArray[0], newArray[0]);
        assertEquals(storedArray[0], newArray[1]);
        assertEquals(storedArray[expectedSize - 2], newArray[expectedSize - 1]);
        assertEquals(expectedSize, persistentCollection.size());
    }

    @Test
    public void testInsertInMiddleOfCollection() {
        Object[] storedArray = persistentCollection.toArray();
        int expectedSize = persistentCollection.size() + 1;
        persistentCollection.insert("about to be inserted", 2);
        Object[] newArray = persistentCollection.toArray();
        assertNotEquals(storedArray, newArray);
        assertNotEquals(storedArray[2], newArray[2]);
        assertEquals(storedArray[2], newArray[3]);
        assertEquals(storedArray[expectedSize - 2], newArray[expectedSize - 1]);
        assertEquals(expectedSize, persistentCollection.size());
    }

    @Test
    public void testInsertAtEndOfCollection() {
        Object[] storedArray = persistentCollection.toArray();
        int expectedSize = persistentCollection.size() + 1;
        persistentCollection.insert("here we go", persistentCollection.size());
        Object[] newArray = persistentCollection.toArray();
        assertNotEquals(storedArray, newArray);
        assertNotEquals(storedArray[persistentCollection.size() - 2], newArray[persistentCollection.size() - 1]);
        assertEquals(storedArray[0], newArray[0]);
        assertEquals(storedArray[expectedSize - 2], newArray[expectedSize - 2]);
        assertEquals(expectedSize, persistentCollection.size());
    }

    @Test
    public void testPassNullAsElementToInsertShouldThrow() {
        assertThrows(NullPointerException.class, () -> emptyCollection.insert(null, 3));
        assertThrows(NullPointerException.class, () -> fiveElementCollection.insert(null, 3));
        assertThrows(NullPointerException.class, () -> fullCollection.insert(null, 3));
    }

    @Test
    public void testInsertElementAtIndexOutOfRangeShouldThrow() {
        assertThrows(IndexOutOfBoundsException.class, () -> fiveElementCollection.insert("not inserted", 6));
        assertThrows(IndexOutOfBoundsException.class, () -> fiveElementCollection.insert("nope", -1));
        assertThrows(IndexOutOfBoundsException.class, () -> fullCollection.insert("still not inserted", 8));
    }

    @Test
    public void testIndexOfExistingElementInCollection() {
        assertEquals(0, persistentCollection.indexOf(123));
    }

    @Test
    public void testIndexOfElementNotExistingInCollectionContainingElements() {
        assertEquals(-1, persistentCollection.indexOf("not in here"));
    }

    @Test
    public void testIndexOfElementInEmptyCollection() {
        assertEquals(-1, emptyCollection.indexOf("nothing is in here"));
    }

    @Test
    public void testIndexOfNull() {
        assertEquals(-1, emptyCollection.indexOf(null));
        assertEquals(-1, persistentCollection.indexOf(null));
    }


    @Test
    public void testRemoveExistingElementAtIndexInCollectionContainingElements() {
        Object[] storedArray = persistentCollection.toArray();
        int expectedSize = persistentCollection.size() - 1;
        persistentCollection.remove(1);
        Object[] newArray = persistentCollection.toArray();
        assertNotEquals(storedArray, newArray);
        assertNotEquals(storedArray[1], newArray[1]);
        assertEquals(storedArray[2], newArray[1]);
        assertEquals(storedArray[expectedSize-2], newArray[expectedSize-2]);
        assertEquals(expectedSize, persistentCollection.size());
    }

    @Test
    public void testRemoveElementAtIndexOutOfRangeShouldThrow() {
        assertThrows(IndexOutOfBoundsException.class, () -> persistentCollection.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> persistentCollection.remove(3));
    }

    @Test
    public void testRemoveExistingElementFromCollectionContainingElements() {
        Object[] storedArray = persistentCollection.toArray();
        int expectedSize = persistentCollection.size() - 1;
        assertTrue(persistentCollection.remove(Integer.valueOf(123)));
        Object[] newArray = persistentCollection.toArray();
        assertNotEquals(storedArray, newArray);
        assertNotEquals(storedArray[1], newArray[1]);
        assertEquals(storedArray[2], newArray[1]);
        assertEquals(storedArray[expectedSize-1], newArray[expectedSize-2]);
        assertEquals(expectedSize, persistentCollection.size());
    }

    @Test
    public void testRemoveNonexistentElementFromCollectionContainingElements() {
        assertFalse(persistentCollection.remove(Integer.valueOf(101112)));
    }

    @Test
    public void testRemoveNullAsElementFromCollectionContainingElements() {
        assertFalse(persistentCollection.remove(null));
    }

    @Test
    public void testCollectionContainingElementsContainsExistingElement() {
        assertTrue(persistentCollection.contains(456));
    }

    @Test
    public void testCollectionContainingElementsContainsNonexistentElement() {
        assertFalse(persistentCollection.contains("are you in here"));
    }

    @Test
    public void testCollectionContainingElementsContainsNull() {
        assertFalse(persistentCollection.contains(null));
    }

    @Test
    public void testEqualsEmptyCollections() {
        assertEquals(new ArrayIndexedCollection<>(emptyCollection), emptyCollection);
    }

    @Test
    public void testEqualsCollectionsContainingElements() {
        assertEquals(new ArrayIndexedCollection<>(fiveElementCollection), fiveElementCollection);
    }

    @Test
    public void testHashCodeEmptyCollection() {
        assertEquals(emptyCollection.hashCode(), new ArrayIndexedCollection<>(emptyCollection).hashCode());
    }

    @Test
    public void testHashCodeCollectionsContainingElement() {
        assertEquals(fiveElementCollection.hashCode(), new ArrayIndexedCollection<>(fiveElementCollection).hashCode());
    }

}
