package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The {@code LinkedListIndexedCollection} class represents the implementation of a linked list-backed {@link Collection} of {@code Object} instances.
 * Duplicate elements are allowed, while the storage of {@code null} references is not.
 *
 * @param <T> type of objects stored in the collection.
 * @author mirtamoslavac
 * @version 3.1
 */
public class LinkedListIndexedCollection<T> implements List<T> {

    /**
     * {@code ListNode} is a private static class that represents a single node within a {@link LinkedListIndexedCollection}.
     *
     * @param <T> type of value stored in the node.
     * @author mirtamoslavac
     * @version 3.0
     */
    private static class ListNode<T> {

        /**
         * "Pointer" to the previous list node.
         */
        private ListNode<T> previous;

        /**
         * "Pointer" to the next list node.
         */
        private ListNode<T> next;

        /**
         * Reference for storing a certain value in the current node.
         */
        private T value;

        /**
         * Stores only the given value, without positioning the node within the linked-list.
         *
         * @param value stored node value
         */
        private ListNode(T value) {
            this(null, null, value);
        }

        /**
         * Stores the given value and positions the node within the linked-list.
         *
         * @param previous previous node
         * @param next     next node
         * @param value    stored node value
         */
        private ListNode(ListNode<T> previous, ListNode<T> next, T value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ListNode)) return false;
            ListNode<?> listNode = (ListNode<?>) o;
            return Objects.equals(this.value, listNode.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.value);
        }

    }

    /**
     * Stores the number of elements in the current collection, which in this case is the number of nodes in the list.
     */
    private int size;
    /**
     * Reference to the first node of the linked-list.
     */
    private ListNode<T> first;
    /**
     * Reference to the last node of the linked-list.
     */
    private ListNode<T> last;

    /**
     * Counter of how many times a modification on this current collection has occurred.
     */
    private long modificationCount = 0;

    /**
     * Default constructor that creates an empty instance of {@code LinkedListIndexedCollection}.
     */
    public LinkedListIndexedCollection() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * Constructor that receives another {@link Collection} and copies its elements into this new instance of {@code LinkedListIndexedCollection}.
     *
     * @param other reference to some other {@code Collection}.
     */
    public LinkedListIndexedCollection(Collection<? extends T> other) {
        this.addAll(other);
    }

    @Override
    public T get(int index) {
        return this.getNode(Objects.checkIndex(index, this.size)).value;
    }

    @Override
    public void insert(T value, int position) {
        if (value == null) throw new NullPointerException("The passed value cannot be null!");
        position = Objects.checkIndex(position, this.size + 1);

        ListNode<T> insertedNode = new ListNode<>(value);
        if (this.first == null) {
            this.first = this.last = insertedNode;
        } else if (position == 0) {
            insertedNode.next = this.first;
            this.first.previous = insertedNode;
            this.first = insertedNode;
        } else if (position == this.size) {
            this.last.next = insertedNode;
            insertedNode.previous = this.last;
            this.last = insertedNode;
        } else {
            ListNode<T> currentNode = getNode(position);
            currentNode.previous.next = insertedNode;
            insertedNode.previous = currentNode.previous;
            insertedNode.next = currentNode;
            currentNode.previous = insertedNode;
        }
        this.size++;
        this.modificationCount++;
    }

    @Override
    public int indexOf(Object value) {
        if(value == null){
            return -1;
        }

        int returnValue = -1;

        ListNode<T> currentNode = this.first;
        for (int i = 0; currentNode != null; i++) {
            if(currentNode.value.equals(value)){
                returnValue = i;
                break;
            }
            currentNode = currentNode.next;
        }

        return returnValue;
    }

    @Override
    public void remove(int index) {
        index = Objects.checkIndex(index, this.size);

        if (this.first != null && this.first == this.last){
            this.first = this.last = null;
        } else if (this.first != null && index == 0){
            this.first.next.previous = null;
            this.first.value = null;
            this.first = this.first.next;
        } else if (index == this.size - 1){
            this.last.previous.next = null;
            this.last.value = null;
            this.last = this.last.previous;
        } else {
            ListNode<T> removedNode = this.getNode(index);
            removedNode.previous.next = removedNode.next;
            removedNode.next.previous = removedNode.previous;
            removedNode.previous = null;
            removedNode.next = null;
            removedNode.value = null;
        }
        this.size--;
        this.modificationCount++;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(T value) {
        if (value == null) throw new NullPointerException("The passed value cannot be null!");

        ListNode<T> addedNode = new ListNode<>(value);
        if (this.first == null) {
            this.first = this.last = addedNode;
        } else {
            this.last.next = addedNode;
            addedNode.previous = this.last;
            this.last = addedNode;
        }

        this.size++;
        this.modificationCount++;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
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
    public Object[] toArray() {
        Object[] newArray = new Object[this.size];

        ListNode<T> currentNode = this.first;
        for (int i = 0, listSize = this.size; currentNode != null && i < listSize; i++) {
            newArray[i] = currentNode.value;
            currentNode = currentNode.next;
        }

        return newArray;
    }

    /**
     * Removes all elements from the collection by “forgetting” about current linked-list.
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;

        this.modificationCount++;
    }


    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new LinkedListIndexedCollection.LinkedListElementsGetter<>(this);
    }

    /**
     * The {@code LinkedListElementsGetter} is an implementation of {@link ElementsGetter} adapted for the {@link LinkedListIndexedCollection}.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private static class LinkedListElementsGetter<T> implements ElementsGetter<T> {

        /**
         * The collection whose elements will be fetched on demand, in order as they appear in it.
         */
        private final LinkedListIndexedCollection<T> linkedListCollection;

        /**
         * The next node that is to be fetched.
         */
        private ListNode<T> nextNode;

        /**
         * The number of modifications made on the collection at the moment of instancing this current elements getter.
         */
        private final long savedModificationCount;

        /**
         * Initializes the getter to the starting position.
         *
         * @param linkedListCollection collection whose elements will be fetched on demand.
         * @throws NullPointerException when the given collection is {@code null}.
         */
        private LinkedListElementsGetter(LinkedListIndexedCollection<T> linkedListCollection) {
            if (linkedListCollection == null) throw new NullPointerException("The given collection cannot be null!");
            this.linkedListCollection = linkedListCollection;
            this.nextNode = linkedListCollection.first;
            this.savedModificationCount = this.linkedListCollection.modificationCount;
        }


        @Override
        public boolean hasNextElement() {
            if (this.savedModificationCount != this.linkedListCollection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified after instancing this element getter!");
            }

            return this.nextNode != null;
        }


        @Override
        public T getNextElement() {
            if (this.savedModificationCount != this.linkedListCollection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified after instancing this element getter!");
            }
            if (!hasNextElement()) {
                throw new NoSuchElementException("There are no elements in this collection that remain unfetched!");
            }

            T valueToReturn = this.nextNode.value;
            this.nextNode = nextNode.next;

            return valueToReturn;
        }
    }

    /**
     * Fetches the node at the given {@code index}.
     *
     * @param index position of the wanted node.
     * @return {@code ListNode} instance at the given {@code index} in the linked-list.
     */
    private ListNode<T> getNode(int index) {
        ListNode<T> currentNode;

        if (index <= (this.size - 1) / 2) {
            currentNode = this.first;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        } else {
            currentNode = this.last;
            for (int i = this.size - 1; i > index; i--) {
                currentNode = currentNode.previous;
            }
        }

        return currentNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedListIndexedCollection)) return false;
        LinkedListIndexedCollection<?> that = (LinkedListIndexedCollection<?>) o;
        return this.size == that.size &&
                this.modificationCount == that.modificationCount &&
                Objects.equals(this.first, that.first) &&
                Objects.equals(this.last, that.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.size, this.first, this.last, this.modificationCount);
    }
}
