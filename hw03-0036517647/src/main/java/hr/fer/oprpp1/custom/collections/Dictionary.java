package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * The {@code Dictionary} class is a collection of key-value pairs.
 * It serves as the Adaptor in the Adapter pattern.
 *
 * @param <K> type of every key object in the current dictionary.
 * @param <V> type of every value object in the current dictionary.
 * @author mirtamoslavac
 * @version 1.0
 */
public class Dictionary<K,V> {

    /**
     * The Adaptee in the adapter pattern, in this case an {@link ArrayIndexedCollection}, in which the key-value pairs will be stored.
     */
    private ArrayIndexedCollection<DictionaryEntry<K,V>> dictionaryEntries;

    /**
     *  Default constructor that creates a new instance of both the adaptor ({@code Dictionary}) and the adaptee ({@link ArrayIndexedCollection}).
     */
    public Dictionary() {
        this.dictionaryEntries = new ArrayIndexedCollection<>();
    }

    /**
     * The {@code DictionaryEntry} class represents a single key-value pair within the dictionary.
     *
     * @param <K> type of the key object in the dictionary entry.
     * @param <V> type of the value object in the dictionary entry.
     */
    private static class DictionaryEntry<K,V> {
        /**
         * The key to the the current entry.
         */
        private final K key;

        /**
         * The value of the current entry.
         */
        private V value;

        /**
         * Creates a new {@code DictionaryEntry} with the given {@code key} and {@code value}.
         *
         * @param key the key to the new entry.
         * @param value the value of the new entry.
         * @throws NullPointerException when the given {@code key} is {@code null}.
         */
        DictionaryEntry(K key, V value) {
            if (key == null) throw new NullPointerException("The given key cannot be null!");

            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DictionaryEntry)) return false;
            DictionaryEntry<?, ?> that = (DictionaryEntry<?, ?>) o;
            return Objects.equals(this.key, that.key) &&
                    Objects.equals(this.value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.key, this.value);
        }
    }

    /**
     * Determines whether the current dictionary is empty.
     *
     * @return {@code true} if the current dictionary contains no entries, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.dictionaryEntries.isEmpty();
    }

    /**
     * Determines the number of entries within the current dictionary.
     *
     * @return the number of currently stored entries in the current dictionary.
     */
    public int size() {
        return this.dictionaryEntries.size();
    }

    /**
     * Removes all elements from the dictionary.
     */
    public void clear() {
        this.dictionaryEntries.clear();
    }

    /**
     * Inserts the given key and value as a {@link DictionaryEntry} in the current dictionary.
     * If an entry with the same {@code key} that is given already exists, then the old {@code value} will be overwritten by the new one.
     *
     * @param key the key of the new {@code DictionaryEntry} instance.
     * @param value the value of the new {@code DictionaryEntry} instance.
     * @throws NullPointerException when the given {@code key} is {@code null}.
     * @return old {@code value} if an entry with the same {@code key} already existed, {@code null} otherwise.
     */
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("The given key cannot be null!");

        try {
            V oldValue = this.get(key);
            this.dictionaryEntries.insert(new DictionaryEntry<>(key, value), this.dictionaryEntries.indexOf(new DictionaryEntry<>(key, oldValue)));
            return oldValue;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Retrieves the entry in the current dictionary that has the same key as the given {@code key}.
     *
     * @param key the key of the wanted {@code DictionaryEntry} instance.
     * @throws NullPointerException when the given {@code key} is {@code null}.
     * @return {@code value} if the matched entry contains a non-{@code null} value, {@code null} otherwise.
     */
    public V get(Object key) {
        if (key == null) throw new NullPointerException("The given key cannot be null!");

        try {
            return this.dictionaryEntries.get(this.dictionaryEntries.indexOf(key)).value;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Removes the entry from the current dictionary that has the same key as the given {@code key}.
     *
     * @param key the key of the wanted {@code DictionaryEntry} instance.
     * @throws NullPointerException when the given {@code key} is {@code null}.
     * @return {@code value} if the matched and removed entry contains a non-{@code null} value, {@code null} otherwise.
     */
    public V remove(K key) {
        if (key == null) throw new NullPointerException("The given key cannot be null!");

        V value = this.get(key);
        if (value == null) return null;

        if (this.dictionaryEntries.remove(new DictionaryEntry<>(key, value))) return value;
        else return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dictionary)) return false;
        Dictionary<?, ?> that = (Dictionary<?, ?>) o;
        return Objects.equals(this.dictionaryEntries, that.dictionaryEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dictionaryEntries);
    }
}
