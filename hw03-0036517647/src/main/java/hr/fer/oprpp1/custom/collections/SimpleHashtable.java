package hr.fer.oprpp1.custom.collections;

import java.util.*;

import static java.lang.Math.abs;

/**
 * The {@code SimpleHashtable} class is a hashed table of key-value pairs.
 *
 * @param <K> type of every key object in the current hashtable.
 * @param <V> type of every value object in the current hashtable.
 * @author mirtamoslavac
 * @version 1.1
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K,V>>{

    /**
     * Array representing the hashtable slots.
     */
    private TableEntry<K, V>[] hashtable;

    /**
     * The number of key-value pairs stored in the current hashtable.
     */
    private int size;

    /**
     * Counter of how many times a modification on the current hashtable has occurred.
     */
    private long modificationCount = 0;

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
     * Creates an instance of {@code SimpleHashtable} and sets the number of slots in the table to the first power of two equal to or larger than the given {@code capacity}.
     *
     * @param capacity the proposed number of slots for the new {@code SimpleHashtable} instance.
     * @throws IllegalArgumentException when the given initialNumberOfSlots is equal to or smaller than 1.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("The initial capacity of the array-backed collection should be larger than 1!");

        this.hashtable = (TableEntry<K, V>[]) new TableEntry[firstSquare(capacity)];
        this.size = 0;
    }

    /**
     * The {@code TableEntry} class represents a single key-value pair within the hashtable.
     *
     * @param <K> type of the key object in the dictionary entry.
     * @param <V> type of the value object in the dictionary entry.
     * @author mirtamoslavac
     * @version 1.0
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
        private TableEntry(K key, V value) {
            if (key == null) throw new NullPointerException("The given key cannot be null!");

            this.key = key;
            this.value = value;
            this.next = null;
        }

        /**
         * Fetches the key of the current table entry.
         *
         * @return the key of the current table entry
         */
        public K getKey() {
            return key;
        }

        /**
         * Fetches the value of the current table entry.
         *
         * @return value of the current table entry.
         */
        public V getValue() {
            return value;
        }

        /**
         * Stores the new given {@code value} for the current table entry.
         *
         * @param value new value of the current table entry.
         */
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
     * Inserts the given key and value as a {@link TableEntry} instance in the current hashtable.
     * If an entry with the same {@code key} that is given already exists, then the old {@code value} will be overwritten by the new one.
     *
     * @param key the key of the new {@code TableEntry} instance.
     * @param value the value of the new {@code TableEntry} instance.
     * @throws NullPointerException when the given {@code key} is {@code null}.
     * @return old {@code value} if an entry with the same {@code key} already existed, {@code null} otherwise.
     */
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("The given key cannot be null!");

        this.checkOccupancy();

        int slot = getSlot(key);

        if (this.containsKey(key)) {
            TableEntry<K, V> hashtableElement = this.hashtable[slot];
            while (hashtableElement != null) {
                if (hashtableElement.key.equals(key)) {
                    V oldValue = hashtableElement.value;
                    hashtableElement.setValue(value);

                    return oldValue;
                }

                hashtableElement = hashtableElement.next;
            }
        }

        TableEntry<K, V> hashtableElement = this.hashtable[slot];
        if (hashtableElement == null) {
            this.hashtable[slot] = new TableEntry<>(key, value);
            this.size++;
            this.modificationCount++;

            return null;
        }

        for(; hashtableElement.next != null; hashtableElement = hashtableElement.next);
        hashtableElement.next = new TableEntry<>(key, value);
        this.size++;
        this.modificationCount++;

        return null;
    }

    /**
     * Fetches the {@code value} of the table entry whose {@code key} equals the given {@code key}.
     *
     * @param key {@code key} to the entry whose {@code value} is wanted.
     * @return {@code value} that is stored in an existing entry, {@code null} otherwise (not making a difference between the {@code key} not existing and the {@code value}
     * being {@code null}).
     */
    public V get(Object key) {
        if (key == null) return null;

        TableEntry<K, V> hashtableElement = this.hashtable[getSlot(key)];
        while (hashtableElement != null) {
            if (hashtableElement.key.equals(key))
                return hashtableElement.value;

            hashtableElement = hashtableElement.next;
        }

        return null;

    }

    /**
     * Determines the size of the current hashtable , also known as the number of entries within it.
     *
     * @return the number of currently stored key-value pairs in the current hashtable.
     */
    public int size() {
        return this.size;
    }

    /**
     * Checks whether the current hashtable contains an entry whose {@code key} equals the given {@code key}.
     *
     * @param key {@code key} that is sought after in the current collection.
     * @return {@code true} if an entry with that {@code key} exists, {@code false} otherwise.
     */
    public boolean containsKey(Object key) {
        if (key == null) return false;

        if (this.isValueNull(key)) return true;

        return this.get(key) != null;
    }

    /**
     * Checks whether the current hashtable contains an entry whose {@code value} equals the given {@code value}.
     *
     * @param value {@code value} that is sought after in the current collection.
     * @return {@code true} when the first entry with that {@code value} is found, {@code false} if no entries contain it.
     */
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

    /**
     * Removes the {@link TableEntry} instance in the current hashtable whose {@code key} equals the given {@code key}.
     * If the to be removed entry is not the only one in the slot, then the rest of the elements in the slot adjust to the new order.
     *
     * @param key {@code key} of the entry that is to be removed.
     * @return the {@code value} of the removed entry or {@code null} if the given {@code key} is {@code null} or the entry with that {@code key} is not in the current hashtable.
     */
    public V remove(Object key) {
        if (key == null || !this.containsKey(key)) return null;

        V value = null;
        int slot = getSlot(key);

        TableEntry<K,V> hashtableElement = this.hashtable[slot];
        if (hashtableElement.key.equals(key)) {
            value = hashtableElement.value;

            this.hashtable[slot] = hashtableElement.next;
            this.size--;
            this.modificationCount++;
        } else {
            TableEntry<K,V> previous;

            while (hashtableElement.next != null) {
                previous = hashtableElement;
                hashtableElement = hashtableElement.next;

                if (hashtableElement.key.equals(key)) {
                    value = hashtableElement.value;
                    previous.next = hashtableElement.next;

                    this.size--;
                    this.modificationCount++;
                    break;
                }
            }
        }

        return value;
    }

    /**
     * Determines whether the current hashtable is empty.
     *
     * @return {@code true} if the current hashtable is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (TableEntry<K, V> hashtableElement : this.hashtable) {
            while (hashtableElement != null) {
                sb.append(hashtableElement.key.toString()).append("=");

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

    /**
     * Creates and returns an array of {@link TableEntry} instances stored in the current hashtable, ordered first by the slot number, then by the order within the same slot.
     *
     * @return array of stored entry references.
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K,V>[] toArray() {

        TableEntry<K,V>[] array = (TableEntry<K, V>[]) new TableEntry[this.size];
        int index = 0;

        for (TableEntry<K, V> hashtableElement : this.hashtable) {
            while (hashtableElement != null) {
                array[index++] = hashtableElement;
                hashtableElement = hashtableElement.next;
            }
        }

        return array;
    }

    /**
     * Removes all entries from the current hashtable. The {@code hashtable} array keeps the capacity that it had prior to being cleared and all existing elements are set to null.
     */
    public void clear() {
        Arrays.fill(this.hashtable, null);
        this.size = 0;
        this.modificationCount++;
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

    /**
     * Calculates the slot in the current hashtable where the {@code key} belongs to.
     *
     * @param key {@code key} whose slot in the hashtable is to be calculated.
     * @return the calculated slot in the current hashtable.
     */
    private int getSlot(Object key) {
        return abs(key.hashCode()) % this.hashtable.length;
    }

    /**
     * Calculates the first power of two larger than or equaling the given {@code initialNumberOfSlots}.
     *
     * @param initialNumberOfSlots the previous number of slots of the current hashtable.
     * @return {@code initialNumberOfSlots}, if it itself is a power of two, or the first power of two larger than the given {@code initialNumberOfSlots}.
     */
    private int firstSquare(int initialNumberOfSlots) {
        if (initialNumberOfSlots % 2 == 0) {
            return initialNumberOfSlots;
        }

        int newCapacity = 1;
        for (; newCapacity < initialNumberOfSlots; newCapacity *= 2);

        return newCapacity;
    }

    /**
     * Checks if the determined {@code DEFAULT_OVERCAPACITY_FACTOR} for the occupancy of the current hashtable has been reached and scales its size by the {@code RESIZING_FACTOR}
     * (while rearranging the already existing entries), if needed.
     */
    @SuppressWarnings("unchecked")
    private void checkOccupancy() {
        if (this.size / (1. * this.hashtable.length) < DEFAULT_OVERCAPACITY_FACTOR) return;
        TableEntry<K, V>[] oldTable = this.toArray();
        this.hashtable = (TableEntry<K, V>[]) new TableEntry[this.hashtable.length * RESIZING_FACTOR];

        for (TableEntry<K, V> hashtableEntry : oldTable) {
            int slot = getSlot(hashtableEntry.key);
            TableEntry<K, V> currentEntry = this.hashtable[slot];

            TableEntry<K, V> hashtableElement = this.hashtable[slot];
            TableEntry<K, V> newHashtableEntry = new TableEntry<>(hashtableEntry.key, hashtableEntry.value);
            if (currentEntry == null) {
                this.hashtable[slot] = newHashtableEntry;
            } else {
                for(; hashtableElement.next != null; hashtableElement = hashtableElement.next);
                hashtableElement.next = newHashtableEntry;
            }
        }

        this.modificationCount++;
    }

    /**
     * Determines whether the {@code value} of the entry is {@code null}.
     * @param key {@code key} of the required entry.
     * @return {@code true} if the entry's value is {@code null}, {@code false} otherwise.
     */
    private boolean isValueNull(Object key) {
        TableEntry<K, V> hashtableElement = this.hashtable[getSlot(key)];
        while (hashtableElement != null) {
            if (hashtableElement.key.equals(key) && hashtableElement.value == null)
                return true;

            hashtableElement = hashtableElement.next;
        }

        return false;

    }

    /**
     * The {@code IteratorImpl} class represents an implementation of the {@link Iterator} for the {@link SimpleHashtable} class.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {

        /**
         * The entry in the current hashtable that was last fetched.
         */
        private TableEntry<K,V> currentTableEntry;

        /**
         * The entry in the current hashtable that is to be fetched next.
         */
        private TableEntry<K,V> nextTableEntry;

        /**
         * The slot in the current hashtable that is being processed by the current iterator.
         */
        private int currentSlot;

        /**
         * The number of modifications made on the collection at the moment of instancing the current {@code IteratorImpl} instance or when removing an entry within it occurs.
         */
        private long savedModificationCount;

        /**
         * Default constructor that creates a new {@code IteratorImpl} and finds the first table entry to be processed by the iterator.
         */
        private IteratorImpl() {
            this.currentTableEntry = null;
            this.nextTableEntry = null;
            this.currentSlot = 0;
            this.savedModificationCount = modificationCount;

            this.findNextTableEntry();
        }

        @Override
        public boolean hasNext() {
            if (this.savedModificationCount != modificationCount) throw new ConcurrentModificationException("The hashtable has been modified outside of the already instanced iterator!");
            return nextTableEntry != null;
        }

        @Override
        public SimpleHashtable.TableEntry<K,V> next() {
            if (this.nextTableEntry == null) throw new NoSuchElementException("There are no more elements in this hashtable available for iteration!");

            this.currentTableEntry = this.nextTableEntry;
            this.findNextTableEntry();

            return this.currentTableEntry;
        }

        @Override
        public void remove() {
            if (this.savedModificationCount != modificationCount) throw new ConcurrentModificationException("The hashtable has been modified outside of the already instanced iterator!");
            if (this.currentTableEntry == null) throw new IllegalStateException("Cannot remove the same element twice after calling the next method!");

            SimpleHashtable.this.remove(this.currentTableEntry.key);
            this.savedModificationCount = modificationCount;

            this.currentTableEntry = null;
        }

        /**
         * Finds the next {@link TableEntry} instance in the current hashtable that is to be processed by the current iterator.
         */
        private void findNextTableEntry() {
            if (this.nextTableEntry != null) this.nextTableEntry = this.nextTableEntry.next;

            while (this.nextTableEntry == null) {
                if (++this.currentSlot >= hashtable.length) break;

                this.nextTableEntry = hashtable[this.currentSlot];
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleHashtable)) return false;
        SimpleHashtable<?, ?> that = (SimpleHashtable<?, ?>) o;
        return size == that.size &&
                modificationCount == that.modificationCount &&
                Arrays.equals(hashtable, that.hashtable);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size, modificationCount);
        result = 31 * result + Arrays.hashCode(hashtable);
        return result;
    }
}
