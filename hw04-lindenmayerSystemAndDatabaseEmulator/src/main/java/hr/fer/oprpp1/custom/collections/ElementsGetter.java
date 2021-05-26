package hr.fer.oprpp1.custom.collections;

/**
 * The {@code ElementsGetter} interface fetches elements of a collection on demand.
 *
 * @param <T> type of objects stored in the elements getter.
 * @author mirtamoslavac
 * @version 2.0
 */
public interface ElementsGetter<T> {

    /**
     * Determines whether the collection has elements that have yet to be fetched.
     *
     * @return {@code true} if the collection has any more elements to retrieve, {@code false} if otherwise.
     */
    boolean hasNextElement();

    /**
     * Fetches the first element of the collection that hasn't yet been fetched.
     *
     * @throws java.util.ConcurrentModificationException when attempting to fetch an element after the collection of the getter had been modified.
     * @throws java.util.NoSuchElementException when trying to fetch an element, but no unfetched elements are left.
     * @return element of the collection that hasn't been fetched
     */
    T getNextElement();

    /**
     * Calls a given {@link Processor} instance on all remaining unfetched elements of the collection.

     * @param p processor used to process remaining elements.
     * @throws NullPointerException when the given processor is {@code null}.
     */
    default void processRemaining(Processor<? super T> p) {
        if (p == null) throw new NullPointerException("The given processor cannot be null!");
        while (this.hasNextElement()) {
            p.process(getNextElement());
        }
    }
}
