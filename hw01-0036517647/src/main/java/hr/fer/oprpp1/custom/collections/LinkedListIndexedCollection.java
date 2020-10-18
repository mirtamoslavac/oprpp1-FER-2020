package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * The {@code LinkedListIndexedCollection} class represents the implementation of a linked list-backed {@link Collection} of {@code Object} instances.
 * Duplicate elements are allowed, while the storage of {@code null} references is not.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LinkedListIndexedCollection extends Collection {

    /**
     * {@code ListNode} is a private static class that represents a single node within a {@link LinkedListIndexedCollection}.
     */
    private static class ListNode {

        /**
         * "Pointer" to the previous list node.
         */
        private ListNode previous;

        /**
         * "Pointer" to the next list node.
         */
        private ListNode next;

        /**
         * Reference for storing a certain value in the current node.
         */
        private Object value;

        /**
         * Stores only the given value, without positioning the node within the linked-list.
         *
         * @param value stored node value
         */
        private ListNode(Object value) {
            this(null, null, value);
        }

        /**
         * Stores the given value and positions the node within the linked-list.
         *
         * @param previous previous node
         * @param next     next node
         * @param value    stored node value
         */
        private ListNode(ListNode previous, ListNode next, Object value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }


    }

    /**
     * Stores the number of elements in the current collection, which in this case is the number of nodes in the list.
     */
    private int size;
    /**
     * Reference to the first node of the linked-list.
     */
    private ListNode first;
    /**
     * Reference to the last node of the linked-list.
     */
    private ListNode last;

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
    public LinkedListIndexedCollection(Collection other) {
        this.addAll(other);
    }

    /**
     * Retrieves the element stored in the current collection at the position {@code index}.
     *
     * @param index position of the element in the current collection.
     * @throws IndexOutOfBoundsException when index is not within the range from 0 to {@code this.size-1}.
     * @return object at the given position in the current collection.
     */
    public Object get(int index) {
        return this.getNode(Objects.checkIndex(index, this.size)).value;
    }

    /**
     * Inserts the given {@code value} at the given {@code position} in the current collection, while shifting the other already-existing elements one place toward the end.
     *
     * @param value object that is to be inserted into the current collection.
     * @param position position in the current collection where the given object should be inserted.
     * @throws IndexOutOfBoundsException when the given {@code position} is not within the range from 0 to {@code this.size}.
     * @throws NullPointerException when {@code value} is passed as {@code value}.
     */
    public void insert(Object value, int position) {
        if (value == null) throw new NullPointerException("The passed value cannot be null!");
        position = Objects.checkIndex(position, this.size + 1);

        ListNode insertedNode = new ListNode(value);
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
            ListNode currentNode = getNode(position);
            currentNode.previous.next = insertedNode;
            insertedNode.previous = currentNode.previous;
            insertedNode.next = currentNode;
            currentNode.previous = insertedNode;
        }
        size++;
    }

    /**
     * Searches the collection for the first occurrence of the given {@code value}.
     *
     * @param value object that is searched for, can be passed as {@code null}.
     * @return index of the first occurrence of the given {@code value}, -1 if the {@code value} is
     * not found or if the {@code value} passed is {@code null}.
     */
    public int indexOf(Object value) {
        if(value == null){
            return -1;
        }

        int returnValue = -1;

        ListNode currentNode = this.first;
        for (int i = 0; currentNode != null; i++) {
            if(currentNode.value.equals(value)){
                returnValue = i;
                break;
            }
            currentNode = currentNode.next;
        }

        return returnValue;
    }

    /**
     * Removes element that is stored in the current collection at position {@code index}.
     * Shifts the rest of the elements towards the beginning of the linked-list.
     *
     * @param index position of element in the linked-list that is to be fetched.
     * @throws IndexOutOfBoundsException when {@code index} is not within the range from 0 to {@code this.size-1}.
     */
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
            ListNode removedNode = this.getNode(index);
            removedNode.previous.next = removedNode.next;
            removedNode.next.previous = removedNode.previous;
            removedNode.previous = null;
            removedNode.next = null;
            removedNode.value = null;
        }
        size--;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given {@code value} at the end of the current collection.
     *
     * @throws NullPointerException when attempting to add null {@code value} as element.
     * @param value object that is to be added to this collection.
     */
    @Override
    public void add(Object value) {
        if (value == null) throw new NullPointerException("The passed value cannot be null!");

        ListNode addedNode = new ListNode(value);
        if (this.first == null) {
            this.first = this.last = addedNode;
        } else {
            this.last.next = addedNode;
            addedNode.previous = this.last;
            this.last = addedNode;
        }

        this.size++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object value) {
        int index = this.indexOf(value);
        if (index == -1) {
            return false;
        }
        this.remove(this.indexOf(value));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        Object[] newArray = new Object[this.size];

        ListNode currentNode = this.first;
        for (int i = 0, listSize = this.size; currentNode != null && i < listSize; i++) {
            newArray[i] = currentNode.value;
            currentNode = currentNode.next;
        }

        return newArray;
    }

    /**
     * Calls the process function of the passed {@link Processor} instance for every element of the {@code} array of the current collection.
     * The elements are called in the order they are connected within the linked-list.
     *
     * @param processor {@link Processor} instance that is processes objects within the current collection with an operation connected to that instance.
     */
    @Override
    public void forEach(Processor processor) {
        ListNode currentNode = this.first;
        while(currentNode != null){
            processor.process(currentNode.value);
            currentNode = currentNode.next;
        }
    }

    /**
     * Removes all elements from the collection by “forgetting” about current linked-list.
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * Fetches the node at the given {@code index}.
     *
     * @param index position of the wanted node.
     * @return {@code ListNode} instance at the given {@code index} in the linked-list.
     */
    private ListNode getNode(int index) {
        ListNode currentNode;

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
}
