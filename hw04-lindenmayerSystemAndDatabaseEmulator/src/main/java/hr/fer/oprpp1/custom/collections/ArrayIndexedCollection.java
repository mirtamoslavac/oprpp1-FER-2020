package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.System.arraycopy;

/**
 * The {@code ArrayIndexedCollection} class represents the implementation of a resizable array-backed {@link Collection} of {@code Object} instances.
 * Duplicate elements are allowed, while the storage of {@code null} references is not.
 *
 * @param <T> type of objects stored in the collection.
 * @author mirtamoslavac
 * @version 3.1
 */
public class ArrayIndexedCollection<T> implements List<T> {

    /**
     * An array of objects stored in the current collection.
     */
    private T[] elements;

    /**
     * The number of elements within the {@code elements} array of the current collection.
     * Can be lower or equal to the capacity of {@code elements}.
     */
    private int size;

    /**
     * Counter of how many times a modification on this current collection has occurred.
     */
    private long modificationCount = 0;

    /**
     * Default capacity is a constant that will be set as the current collection's capacity
     * when {@code initialCapacity} isn't provided as an argument in the class constructor.
     */
    static final int DEFAULT_CAPACITY = 16;
    /**
     * Factor by which the current capacity of the {@code elements} array is increased when full.
     */
    static final int RESIZING_FACTOR = 2;

    /**
     * Default constructor that creates an instance of {@code ArrayIndexedCollection} and sets the capacity of its {@code elements} array to the default capacity.
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an instance of {@code ArrayIndexedCollection} and sets the capacity of its {@code elements} array to the given {@code initialCapacity}.
     *
     * @param initialCapacity given capacity of the {@code elements} array of the current collection.
     * @throws IllegalArgumentException when the given initialCapacity is equal to or smaller than 1.
     */
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException("The initial capacity of the array-backed collection should be larger than 1!");
        this.elements = (T[]) new Object[initialCapacity];
        this.size = 0;
    }

    /**
     * Creates an instance of {@code ArrayIndexedCollection} and fills its {@code elements} array with elements from the given collection {@code other}.
     * If the {@code DEFAULT_CAPACITY} is smaller than the size of the given collection, the size of the current collection should be set to the size of the given collection.
     *
     * @param other some other collection whose elements are copied into this newly constructed collection.
     * @throws NullPointerException when the given collection is {@code null} or any of its elements are {@code null}.
     * @throws IllegalArgumentException when the given {@code initialCapacity} is equal to or smaller than 1.
     */
    public ArrayIndexedCollection(Collection<? extends T> other) {
        this(other, DEFAULT_CAPACITY);
    }

    /**
     * Creates an instance of {@code ArrayIndexedCollection} with a  {@code elements} array and fills it with elements from the given collection {@code other}.
     * If the  {@code initialCapacity} is smaller than the size of the given collection, the size of the current collection is set to the size of the given collection.
     *
     * @param other some other collection whose elements are copied into this new {@code ArrayIndexedCollection} instance.
     * @param initialCapacity given capacity of the {@code elements} array of the current collection.
     * @throws IllegalArgumentException when the given {@code initialCapacity} is equal to or smaller than 1.
     * @throws NullPointerException when the given collection is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
        if (other == null) throw new NullPointerException("The other collection cannot be null!");
        if (initialCapacity < 1) throw new IllegalArgumentException("The initial capacity of the array-backed collection should be larger than 1!");

        this.elements =(T[]) new Object[max(initialCapacity, other.size())];
        this.addAll(other);

        this.size = other.size();
    }


    @Override
    public int size(){
        return this.size;
    }


    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.elements, this.size);
    }

    /**
     * Adds the the given {@code value} into the current collection into the first empty place in the {@code elements} array.
     * If {@code elements} is full, the array is reallocated and doubled in size.
     */
    @Override
    public void add(T value) {
        if (value == null) throw new NullPointerException("The passed value cannot be null!");

        checkCapacity();
        this.elements[size++] = value;
    }

    /**
     * {@inheritDoc}
     * The {@code elements} array keeps the capacity that it had prior to being cleared and all existing elements are set to {@code null}.
     */
    @Override
    public void clear() {
        Arrays.fill(this.elements, null);

        this.modificationCount++;
    }

    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new ArrayElementsGetter<>(this);
    }

    /**
     * The {@code ArrayElementsGetter} is an implementation of {@link ElementsGetter} adapted for the {@link ArrayIndexedCollection}.
     *
     * @param <T> type of objects stored in the elements getter.
     * @author mirtamoslavac
     * @version 2.0
     */
    private static class ArrayElementsGetter<T> implements ElementsGetter<T> {

        /**
         * The collection whose elements will be fetched on demand, in order as they appear in it.
         */
        private final ArrayIndexedCollection<T> arrayCollection;

        /**
         * Position of the yet unfetched element that is sequentially first to be fetched.
         */
        private int toBeFetched;

        /**
         * The number of modifications made on the collection at the moment of instancing this current elements getter.
         */
        private final long savedModificationCount;

        /**
         * Initializes the getter to the starting position.
         *
         * @param arrayCollection collection whose elements will be fetched on demand.
         * @throws NullPointerException when the given collection is {@code null}.
         */
        private ArrayElementsGetter(ArrayIndexedCollection<T> arrayCollection) {
            if (arrayCollection == null) throw new NullPointerException("The given collection cannot be null!");
            this.arrayCollection = arrayCollection;
            this.toBeFetched = 0;
            this.savedModificationCount = arrayCollection.modificationCount;
        }


        @Override
        public boolean hasNextElement() {
            if (this.savedModificationCount != this.arrayCollection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified after instancing this element getter!");
            }

            return this.toBeFetched < this.arrayCollection.size;
        }


        @Override
        public T getNextElement() {
            if (this.savedModificationCount != this.arrayCollection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified after instancing this element getter!");
            }
            if (!hasNextElement()) {
                throw new NoSuchElementException("There are no elements in this collection that remain unfetched!");
            }

            return this.arrayCollection.get(this.toBeFetched++);
        }
    }

    /**
     * Returns the object that is stored in backing array at position {@code index}.
     *
     * @param index position of element in the {@code elements} that is to be fetched.
     * @return object that is at the given {@code index} in the backing array.
     */
    @Override
    public T get(int index) {
        return this.elements[Objects.checkIndex(index, this.size)];
    }

    /**
     * Inserts the given {@code value} at the given {@code position} in the backing array, while shifting the other already-existing elements in the array one place toward
     * the end.
     *
     * @param position index in the {@code elements} array where the given object should be inserted.
     */
    @Override
    public void insert(T value, int position) {
        if (value == null) throw new NullPointerException("The passed value cannot be null!");
        position = Objects.checkIndex(position, this.size + 1);
        checkCapacity();

        if (this.size - position >= 0) arraycopy(this.elements, position, this.elements, position + 1, this.size - position);

        this.elements[position] = value;
        this.size++;
    }

    @Override
    public int indexOf(Object value) {
        if(value == null){
            return -1;
        }

        int returnValue = -1;
        for (int i = 0, elementNumber = this.size; i < elementNumber; i++) {
            if (value.equals(this.elements[i])) {
                returnValue = i;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Removes the object that is stored in backing array at position {@code index}.
     * Shifts the rest of the elements in the {@code elements} array towards the beginning of the array.
     *
     * @param index position of element in the {@code elements} that is to be fetched.
     */
    @Override
    public void remove(int index) {
        index = Objects.checkIndex(index, this.size);

        for (int i = index, newLastIndex = this.size - 1; i < newLastIndex; i++) {
            this.elements[i] = this.elements[i + 1];
        }

        this.elements[--size] = null;
        this.modificationCount++;
    }


    @Override
    public boolean remove(Object value) {
        int index = this.indexOf(value);
        if (index == -1) {
            return false;
        }
        this.remove(this.indexOf(value));
        return true;
    }


    @Override
    public boolean contains(Object value) {
        return this.indexOf(value) != -1;
    }

    /**
     * Checks whether new addition to the {@code elements} fills the entire array.
     * If so, it reallocates and resizes the array by the defined {@code RESIZING_FACTOR}.
     */
    private void checkCapacity() {
        if (this.elements.length <= this.size) {
            this.elements = Arrays.copyOf(this.elements, this.elements.length * RESIZING_FACTOR);
            this.modificationCount++;
        }
    }

    /**
     * Checks whether some other object {@code o} is "equal to" the current collection.
     *
     * @param o the other object which is compared with the current collection.
     * @return {@code true} if equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayIndexedCollection)) return false;
        ArrayIndexedCollection<?> that = (ArrayIndexedCollection<?>) o;
        return this.size == that.size &&
                Arrays.equals(this.elements, that.elements);
    }

    /**
     * Determines a hash code value for the current collection.
     *
     * @return hash code value for the current collection.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(this.size, this.modificationCount);
        result = 31 * result + Arrays.hashCode(this.elements);
        return result;
    }
}
