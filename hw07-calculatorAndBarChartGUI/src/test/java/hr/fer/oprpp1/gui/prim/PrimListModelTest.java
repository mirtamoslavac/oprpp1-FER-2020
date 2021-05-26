package hr.fer.oprpp1.gui.prim;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrimListModelTest {

    PrimListModel model;
    int[] first10Primes = new int[]{1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
    @BeforeEach
    void setUp() {
        model = new PrimListModel();
    }

    @Test
    public void testConstructor() {
        assertEquals(1, this.model.getSize());
        assertEquals(1, this.model.getElementAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> this.model.getElementAt(1));
    }

    @Test
    public void testGetElementAt() {
        for (int i = 1, size = first10Primes.length; i < size; i++) this.model.next();
        assertEquals(this.first10Primes[5], this.model.getElementAt(5));
    }

    @Test
    public void testNext() {
        for (int i = 1, size = first10Primes.length; i < size; i++) {
            this.model.next();
            assertEquals(this.first10Primes[i], this.model.getElementAt(i).intValue());
        }
        assertEquals(this.first10Primes.length, this.model.getSize());
    }

    @Test
    public void testGetSizeInitialized() {
        assertEquals(1, this.model.getSize());
    }

    @Test
    public void testAddNullListDataListenerThrows() {
        assertThrows(NullPointerException.class, () -> model.addListDataListener(null));
    }

    @Test
    public void testRemoveNullListDataListenerThrows() {
        assertThrows(NullPointerException.class, () -> model.removeListDataListener(null));
    }

}
