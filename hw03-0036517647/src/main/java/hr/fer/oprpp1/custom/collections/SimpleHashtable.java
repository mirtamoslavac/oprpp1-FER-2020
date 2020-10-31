package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * The {@code SimpleHashtable} class is a hashed table of key-value pairs.
 *
 * @param <K> type of every key object in the current hashtable.
 * @param <V> type of every value object in the current hashtable.
 * @author mirtamoslavac
 * @version 1.0
 */
public class SimpleHashtable<K,V> {

    /**
     * Array representing the hashtable slots.
     */
    private TableEntry<K, V>[] hashtable;

    /**
     * The number of key-value pairs stored in the current hashtable.
     */
    private int size;

    /**
     * A constant that represents the default number of slots for a new hashtable when {@code initialNumberOfSlots} isn't provided as an argument in the class constructor.
     */
    static final int DEFAULT_SLOTS = 16;

    /**
     * A constant that represents the percentage of allowed occupied slots for the current hashtable.
     */
    static final double DEFAULT_OVERCAPACITY_FACTOR = 0.75;

    /**
     * Factor by which the current capacity of the {@code hashtable} array is increased when the .
     */
    static final int RESIZING_FACTOR = 2;

    /**
     * Default constructor that creates a new {@code SimpleHashtable} instance with the default number of slots.
     */
    public SimpleHashtable() {
        this(DEFAULT_SLOTS);
    }

    /**
     * Creates an instance of {@code SimpleHashtable} and sets the number of slots in the table to the first power of 2 equal to or larger than the given {@code capacity}.
     *
     * @param capacity the proposed number of slots for the new {@code SimpleHashtable} instance.
     * @throws IllegalArgumentException when the given initialNumberOfSlots is equal to or smaller than 1.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("The initial capacity of the array-backed collection should be larger than 1!");

        this.hashtable = new TableEntry[firstSquare(capacity)];
        this.size = 0;
    }

    /**
     * The {@code TableEntry} class represents a single key-value pair within the hashtable.
     *
     * @param <K> type of the key object in the dictionary entry.
     * @param <V> type of the value object in the dictionary entry.
     */
    public static class TableEntry<K,V> {
        /**
         * The key to the the current entry.
         */
        private final K key;

        /**
         * The value of the current entry.
         */
        private V value;

        /**
         * Reference to the next {@code TableEntry} instance in the same slot of the current hashtable.
         */
        private TableEntry<K,V> next;

        /**
         * Creates a new {@code TableEntry} with the given {@code key} and {@code value}.
         *
         * @param key the key to the new entry.
         * @param value the value of the new entry.
         * @throws NullPointerException when the given {@code key} is {@code null}.
         */
        TableEntry(K key, V value) {
            if (key == null) throw new NullPointerException("The given key cannot be null!");

            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleHashtable.TableEntry)) return false;
            SimpleHashtable.TableEntry<?, ?> that = (SimpleHashtable.TableEntry<?, ?>) o;
            return Objects.equals(this.key, that.key) &&
                    Objects.equals(this.value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.key, this.value);
        }
    }

    /**
     * Inserts the given key and value as a {@link SimpleHashtable.TableEntry} instance in the current hashtable.
     * If an entry with the same {@code key} that is given already exists, then the old {@code value} will be overwritten by the new one.
     *
     * @param key the key of the new {@code TableEntry} instance.
     * @param value the value of the new {@code TableEntry} instance.
     * @throws NullPointerException when the given {@code key} is {@code null}.
     * @return old {@code value} if an entry with the same {@code key} already existed, {@code null} otherwise.
     */
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("The given key cannot be null!");

        int slot = getSlot(key);

        if (this.containsKey(key)) {
            TableEntry<K, V> hashtableElement = this.hashtable[slot];
            while (hashtableElement != null) {
                if (hashtableElement.key.equals(key)) {
                    hashtableElement.setValue(value);
                    return value;
                }

                hashtableElement = hashtableElement.next;
            }
            return value;
        }

        TableEntry<K, V> hashtableElement = this.hashtable[slot];
        if (hashtableElement == null) {
            this.hashtable[slot] = new TableEntry<>(key, value);
            size++;
            return value;
        }

        for(; hashtableElement.next != null; hashtableElement = hashtableElement.next);
        hashtableElement.next = new TableEntry<>(key, value);
        size++;

        return value;
    }

    public V get(Object key) {
        if (key == null) return null; //!(key instanceof K)

        TableEntry<K, V> hashtableElement = this.hashtable[getSlot(key)];
        while (hashtableElement != null) {
            if (hashtableElement.key.equals(key))
                return hashtableElement.value;

            hashtableElement = hashtableElement.next;
        }

        return null;

    }

    public int size() {
        return this.size;
    }

    public boolean containsKey(Object key) {
        if (key == null) return false;
        return this.get(key) != null;
    }

    public boolean containsValue(Object value) {
        for (TableEntry<K, V> hashtableElement : this.hashtable) {
            while (hashtableElement != null) {
                if (hashtableElement.value != null && hashtableElement.value.equals(value) || hashtableElement.value == null && value == null)
                    return true;

                hashtableElement = hashtableElement.next;
            }
        }
        return false;
    }

    public V remove(Object key) {
        if (key == null || !this.containsKey(key)) return null;

        V value = null;
        int slot = getSlot(key);

        TableEntry<K,V> hashtableElement = this.hashtable[slot];
        if (hashtableElement.key.equals(key)) {
            value = hashtableElement.value;
            this.hashtable[slot] = hashtableElement.next;
            this.size--;
        } else {
            TableEntry<K,V> previous;
            while (hashtableElement.next != null) {
                previous = hashtableElement;
                hashtableElement = hashtableElement.next;
                if (hashtableElement.key.equals(key)) {
                    value = hashtableElement.value;
                    previous.next = hashtableElement.next;
                    break;
                }
            }
        }

        return value;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (TableEntry<K, V> hashtableElement : this.hashtable) {
            while (hashtableElement != null) {
                sb.append(hashtableElement.key.toString())
                        .append("=");
                if (hashtableElement.value != null) {
                    sb.append(hashtableElement.value.toString());
                } else {
                    sb.append((Object) null);
                }
                sb.append(", ");

                hashtableElement = hashtableElement.next;
            }
        }

        if (sb.charAt(sb.length()-1) == ' ')
            sb.setLength(sb.length() - 2);

        sb.append("]");

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public TableEntry<K,V>[] toArray() {

        TableEntry<K,V>[] array = new TableEntry[this.size];
        int index = 0;

        for (TableEntry<K, V> hashtableElement : this.hashtable) {
            while (hashtableElement != null) {
                array[index++] = hashtableElement;
                hashtableElement = hashtableElement.next;
            }
        }

        return array;
    }

    private int getSlot(Object key) {
        return abs(key.hashCode()) % this.hashtable.length;
    }

    private int firstSquare(int initialNumberOfSlots) {
        if (initialNumberOfSlots % 2 == 0) {
            return initialNumberOfSlots;
        }

        int newCapacity;
        for (newCapacity = 1; newCapacity < initialNumberOfSlots; newCapacity *= 2);
        return newCapacity;

    }

}
