package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.custom.collections.demo.EvenIntegerTester;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tester class with examples taken from this homework's PDF dedicated to demonstrating certain {@link ElementsGetter} functions.
 */
public class ElementsGetterTest {

    @Test
    public void testGettingAllTheElements() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        assertTrue(getter.hasNextElement());
        assertEquals("Ivo", getter.getNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("Ana", getter.getNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("Jasna", getter.getNextElement());

        assertFalse(getter.hasNextElement());
        assertThrows(NoSuchElementException.class, getter::getNextElement);
    }

    @Test
    public void testCheckingWhetherANextElementExistsMultipleTimes() {
        Collection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("Ivo", getter.getNextElement());

        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("Ana", getter.getNextElement());

        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("Jasna", getter.getNextElement());

        assertFalse(getter.hasNextElement());
        assertFalse(getter.hasNextElement());
    }

    @Test
    public void testGettingAllTheElementsWithoutPreviouslyCheckingIfExisting() {
        Collection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        assertEquals("Ivo", getter.getNextElement());
        assertEquals("Ana", getter.getNextElement());
        assertEquals("Jasna", getter.getNextElement());

        assertThrows(NoSuchElementException.class, getter::getNextElement);
    }

    @Test
    public void testMultipleIndependentGettersForTheSameCollection() {
        Collection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter1 = col.createElementsGetter();
        ElementsGetter getter2 = col.createElementsGetter();

        assertEquals("Ivo", getter1.getNextElement());
        assertEquals("Ana", getter1.getNextElement());
        assertEquals("Ivo", getter2.getNextElement());
        assertEquals("Jasna", getter1.getNextElement());
        assertEquals("Ana", getter2.getNextElement());
    }

    @Test
    public void testMultipleIndependentGettersForTheDifferentCollections() {
        Collection col1 = new ArrayIndexedCollection();
        Collection col2 = new ArrayIndexedCollection();
        col1.add("Ivo");
        col1.add("Ana");
        col1.add("Jasna");
        col2.add("Jasmina");
        col2.add("Štefanija");
        col2.add("Karmela");

        ElementsGetter getter1 = col1.createElementsGetter();
        ElementsGetter getter2 = col1.createElementsGetter();
        ElementsGetter getter3 = col2.createElementsGetter();

        assertEquals("Ivo", getter1.getNextElement());
        assertEquals("Ana", getter1.getNextElement());
        assertEquals("Ivo", getter2.getNextElement());
        assertEquals("Jasmina", getter3.getNextElement());
        assertEquals("Štefanija", getter3.getNextElement());
    }

    @Test
    public void testInvalidGettingAfterModifyingTheCollectionThrows() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        assertEquals("Ivo", getter.getNextElement());
        assertEquals("Ana", getter.getNextElement());
        col.clear();
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }

    @Test
    public void testProcessRemainingUnfetchedElementsWithinACollection() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();
        assertEquals("Ivo", getter.getNextElement());

        getter.processRemaining(System.out::println);
    }

    @Test
    public void testAddAllElementsSatisfyingAGivenConditionFromOneCollectionToAnother() {
        Collection col1 = new LinkedListIndexedCollection();
        Collection col2 = new ArrayIndexedCollection();
        col1.add(2);
        col1.add(3);
        col1.add(4);
        col1.add(5);
        col1.add(6);
        col2.add(12);

        col2.addAllSatisfying(col1, new EvenIntegerTester());

        col2.forEach(System.out::println);
    }

    @Test
    public void testUpcasting() {
        List col1list = new ArrayIndexedCollection();
        List col2list = new LinkedListIndexedCollection();
        col1list.add("Ivana");
        col2list.add("Jasna");
        Collection col3 = col1list;
        Collection col4 = col2list;
        col1list.get(0);
        col2list.get(0);
        //col3.get(0); //cannot resolve method in parent class Collection
        //col4.get(0); //cannot resolve method in parent class Collection
        col1list.forEach(System.out::println); // Ivana
        col2list.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna
    }



}
