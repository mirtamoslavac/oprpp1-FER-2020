package hr.fer.oprpp1.custom.collections;

/**
 * The {@code Collection} interface represents a general collection of objects.
 *
 * @author mirtamoslavac
 * @version 2.0
 */
public interface Collection {

    /**
     * Determines whether the current collection is empty.
     *
     * @return {@code true} if the current collection contains no objects, {@code false} otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Determines the size of the current collection, also known as the number of elements within it.
     *
     * @return the number of currently stored objects in the current collection.
     */
    int size();

    /**
     * Adds the given object into the current collection.
     *
     * @param value object that is to be added to this collection.
     */
    void add(Object value);

    /**
     * Determines whether this current collection contains the given object.
     *
     * @param value object that is to be checked whether it exists in the current collection, can be passed as null.
     * @return {@code true} only if the collection contains given value, {@code false} otherwise.
     */
    boolean contains(Object value);

    /**
     * Removes one occurrence of the given object, that exists within the current collection, from it.
     *
     * @param value object whose single occurrence (of many possible) is to be removed from this collection.
     * @return {@code true} only if the collection contains the given value and removes it, {@code false} if otherwise
     */
    boolean remove(Object value);

    /**
     * Allocates a new array with the size that equals to the size of this collection, fills it with collection content and
     * returns the array.
     *
     * @throws UnsupportedOperationException when attempting to return an unallocated array.
     * @return array filled with objects contained within the current collection.
     */
    Object[] toArray();

    /**
     * Calls {@link Processor#process(Object)} of for each element of this collection.
     *
     * @param processor processor that performs a process for every object within the current collection.
     */
    void forEach(Processor processor);

    /**
     * Adds all elements from the given collection into the current collection.
     * The given collection remains unchanged.
     *
     * @param other the given collection whose elements are to be added to the current collection.
     * @throws NullPointerException when {@code other}(the given collection) is passed as {@code null}.
     */
    default void addAll(Collection other) {

        other.forEach(
                new Processor() {
                /**
                 * Adds the given object to the current collection.
                 *
                 * @param value object upon which some operation is to be performed.
                 */
                @Override
                public void process(Object value) {
                    add(value);
                }
                });
    }

    /**
     * Removes all elements from the current collection.
     */
    void clear();

    /**
     * Creates a new instance of {@link ElementsGetter} for the current collection.
     *
     * @return new instance of {@link ElementsGetter} for the current collection.
     */
    ElementsGetter createElementsGetter();

}
