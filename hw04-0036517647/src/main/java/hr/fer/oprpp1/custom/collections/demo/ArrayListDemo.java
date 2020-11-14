package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Processor;

import java.util.Arrays;

/**
 * {@code ArrayListDemo} is a command-line application that demonstrates certain {@link ArrayIndexedCollection} functionalities.
 */
public class ArrayListDemo {

    /**
     * Method taken from this homework's PDF dedicated to demonstrating certain {@link ArrayIndexedCollection} functionalities.
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        ArrayIndexedCollection<Object> col = new ArrayIndexedCollection<>(2);
        col.add(20);
        col.add("New York");
        col.add("San Francisco"); // here the internal array is reallocated to 4
        System.out.println(col.contains("New York")); // writes: true
        col.remove(1); // removes "New York"; shifts "San Francisco" to position 1
        System.out.println(col.get(1)); // writes: "San Francisco"
        System.out.println(col.size()); // writes: 2
        col.add("Los Angeles");
        ArrayIndexedCollection<Object> col2 = new ArrayIndexedCollection<>(col);
// This is local class representing a Processor which writes objects to System.out
        class P implements Processor<Object> {
            public void process(Object o) {
                System.out.println(o);
            }
        }
        System.out.println("col elements:");
        col.forEach(new P());
        System.out.println("col elements again:");
        System.out.println(Arrays.toString(col.toArray()));
        System.out.println("col2 elements:");
        col2.forEach(new P());
        System.out.println("col2 elements again:");
        System.out.println(Arrays.toString(col2.toArray()));
        System.out.println(col.contains(col2.get(1))); // true
        System.out.println(col2.contains(col.get(1))); // true
        col.remove(Integer.valueOf(20)); // removes 20 from collection (at position 0).

    }
}
