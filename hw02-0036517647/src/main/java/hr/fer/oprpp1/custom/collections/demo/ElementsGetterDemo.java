package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.*;

import java.util.NoSuchElementException;

/**
 * {@code ElementsGetterDemo} is a command-line application that demonstrates certain {@link ElementsGetter} functions.
 */
public class ElementsGetterDemo {

    /**
     * Method taken from this homework's PDF dedicated to demonstrating certain {@link ElementsGetter} functions.
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        System.out.println("DEMO 1\n-----------------");
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

            System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
            System.out.println("Jedan element: " + getter.getNextElement());

            System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
            System.out.println("Jedan element: " + getter.getNextElement());

            System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
            System.out.println("Jedan element: " + getter.getNextElement());

            System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
            //System.out.println("Jedan element: " + getter.getNextElement()); //uncomment for exception

        System.out.println("\n\nDEMO 2\n-----------------");
        col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        getter = col.createElementsGetter();

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());

        System.out.println("\n\nDEMO 3\n-----------------");
        col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        getter = col.createElementsGetter();

        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        //System.out.println("Jedan element: " + getter.getNextElement()); //uncomment for exception

        System.out.println("\n\nDEMO 4\n-----------------");
        col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter1 = col.createElementsGetter();
        ElementsGetter getter2 = col.createElementsGetter();

        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter2.getNextElement());
        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter2.getNextElement());

        System.out.println("\n\nDEMO 5\n-----------------");
        Collection col1 = new ArrayIndexedCollection();
        Collection col2 = new ArrayIndexedCollection();
        col1.add("Ivo");
        col1.add("Ana");
        col1.add("Jasna");
        col2.add("Jasmina");
        col2.add("Štefanija");
        col2.add("Karmela");

        getter1 = col1.createElementsGetter();
        getter2 = col1.createElementsGetter();
        ElementsGetter getter3 = col2.createElementsGetter();

        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter2.getNextElement());
        System.out.println("Jedan element: " + getter3.getNextElement());
        System.out.println("Jedan element: " + getter3.getNextElement());

        System.out.println("\n\nDEMO 6\n-----------------");
        col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        getter = col.createElementsGetter();

        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        col.clear();
        //System.out.println("Jedan element: " + getter.getNextElement()); //uncomment for exception

        System.out.println("\n\nDEMO 7\n-----------------");
        col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        getter = col.createElementsGetter();
        getter.getNextElement();

        getter.processRemaining(System.out::println);

        System.out.println("\n\nDEMO 8\n-----------------");
        col1 = new LinkedListIndexedCollection();
        col2 = new ArrayIndexedCollection();
        col1.add(2);
        col1.add(3);
        col1.add(4);
        col1.add(5);
        col1.add(6);
        col2.add(12);

        col2.addAllSatisfying(col1, new EvenIntegerTester());

        col2.forEach(System.out::println);

        System.out.println("\n\nDEMO 9\n-----------------");
        List col1list = new ArrayIndexedCollection();
        List col2list = new LinkedListIndexedCollection();
        col1list.add("Ivana");
        col2list.add("Jasna");
        Collection col3 = col1list;
        Collection col4 = col2list;
        col1list.get(0);
        col2list.get(0);
        //col3.get(0); // neće se prevesti! Razumijete li zašto?
        //col4.get(0); // neće se prevesti! Razumijete li zašto?
        col1list.forEach(System.out::println); // Ivana
        col2list.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna
    }

}
