package hr.fer.oprpp1.custom.collections;

/**
 * The {@code ElementsGetter} interface fetches elements of a collection on demand.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface ElementsGetter {

    /**
     * Determines whether the collection has elements that have yet to be fetched.
     *
     * @return {@code true} if the collection has any more elements to retrieve, {@code false} if otherwise.
     */
    boolean hasNextElement();

    /**
     * Fetches the first element of the collection that hasn't yet been fetched.
     *
     * @throws java.util.NoSuchElementException when trying to fetch an element, but no unfetched elements are left.
     * @return element of the collection that hasn't been fetched
     */
    Object getNextElement();
}
