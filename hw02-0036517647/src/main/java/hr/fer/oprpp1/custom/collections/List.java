package hr.fer.oprpp1.custom.collections;

/**
 * The {@code List} interface represents a simplified representation of a list collection.
 */
public interface List extends Collection {
    Object get(int index);

    void insert(Object value, int position);

    int indexOf(Object value);

    void remove(int index);
}
