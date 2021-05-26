package hr.fer.oprpp1.custom.collections;

/**
 * The {@code Collection} interface represents a general collection of objects.
 *
 * @param <T> type of objects stored in the collection.
 * @author mirtamoslavac
 * @version 3.0
 */
public interface Collection<T> {

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
     * @throws NullPointerException when attempting to add null {@code value} as element.
     */
    void add(T value);

    /**
     * Determines whether this current collection contains the given object.
     *
     * @param value object that is to be checked whether it exists in the current collection, can be passed as {@code null}.
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
     * Calls {@link Processor#process(Object)} of for each element of the current collection.
     * The elements are called in the order they appear in the collection.
     *
     * @param processor {@link Processor} instance that performs a process for every object within the current collection.
     * @throws NullPointerException when {@code processor} is {@code null}.
     */
    default void forEach(Processor<? super T> processor) {
        if (processor == null) throw new NullPointerException("The given processor cannot be {@code null}!");

        this.createElementsGetter().processRemaining(processor);
    }

    /**
     * Adds all elements from the given collection into the current collection.
     * The given collection remains unchanged.
     *
     * @param other the given collection whose elements are to be added to the current collection.
     * @throws NullPointerException when {@code other}(the given collection) is passed as {@code null}.
     */
    default void addAll(Collection<? extends T> other) {
        if (other == null) throw new NullPointerException("The given other collection is null!");

        other.forEach(this::add);
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
    ElementsGetter<T> createElementsGetter();

    /**
     * Adds all elements of the given collection {@code col} that are accepted by the given {@code tester} at the end of the current collection.
     *
     * @param col collections whose elements could to be added to the current collection.
     * @param tester {@link Tester} instance that checks for each element of {@code col} whether it passes the implemented test.
     * @throws NullPointerException when the given collection or tester are {@code null}.
     */
    default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
        if (col == null) throw new NullPointerException("The given collection cannot be null!");
        if (tester == null) throw new NullPointerException("The given tester cannot be null!");

        col.createElementsGetter().processRemaining(value -> {
            if (tester.test(value)) {
                this.add(value);
            }
        });
    }

}
