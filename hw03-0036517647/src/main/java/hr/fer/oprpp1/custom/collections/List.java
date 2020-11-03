package hr.fer.oprpp1.custom.collections;

/**
 * The {@code List} interface represents a simplified representation of a list collection.
 *
 * @param <T> type of objects stored in the collection.
 * @author mirtamoslavac
 * @version 3.0
 */
public interface List<T> extends Collection<T> {

    /**
     * Retrieves the element stored in the current collection at the position {@code index}.
     *
     * @param index position of the element in the current collection.
     * @throws IndexOutOfBoundsException when index is not within the range from 0 to {@code this.size-1}.
     * @return object at the given position in the current collection.
     */
    T get(int index);

    /**
     * Inserts the given {@code value} at the given {@code position} in the current collection, while shifting the other already-existing elements one place toward the end.
     *
     * @param value object that is to be inserted into the current collection.
     * @param position position in the current collection where the given object should be inserted.
     * @throws IndexOutOfBoundsException when the given {@code position} is not within the range from 0 to {@code this.size}.
     * @throws NullPointerException when {@code value} is passed as {@code value}.
     */
    void insert(T value, int position);

    /**
     * Searches the collection for the first occurrence of the given {@code value}.
     *
     * @param value object that is searched for, can be passed as {@code null}.
     * @return index of the first occurrence of the given {@code value}, -1 if the {@code value} is
     * not found or if the {@code value} passed is {@code null}.
     */
    int indexOf(Object value);

    /**
     * Removes element that is stored in the current collection at position {@code index}.
     * Shifts the rest of the elements towards the beginning of the linked-list.
     *
     * @param index position of element in the linked-list that is to be fetched.
     * @throws IndexOutOfBoundsException when {@code index} is not within the range from 0 to {@code this.size-1}.
     */
    void remove(int index);
}
