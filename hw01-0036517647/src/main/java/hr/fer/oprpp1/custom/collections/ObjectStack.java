package hr.fer.oprpp1.custom.collections;


/**
 * The {@code ObjectStack} class represents the implementation of a stack-like collection.
 * It serves as the Adaptor in the Adapter pattern.
 */
public class ObjectStack {

    /**
     * The Adaptee in the adapter pattern, in this case an {@link ArrayIndexedCollection}.
     */
    private ArrayIndexedCollection adapteeCollection;

    /**
     * Default constructor that creates a new instance of both the adaptor ({@code ObjectStack}) and the adaptee ({@link ArrayIndexedCollection}).
     */
    public ObjectStack() {
        adapteeCollection = new ArrayIndexedCollection();
    }

    /**
     * Determines whether the current collection is empty.
     *
     * @return {@code true} if the current collection contains no objects, {@code false} otherwise.
     */
    public boolean isEmpty(){
        return this.adapteeCollection.isEmpty();
    }

    /**
     * Determines the the number of elements within the current collection.
     *
     * @return the number of currently stored objects in the current collection.
     */
    public int size() {
        return this.adapteeCollection.size();
    }

    /**
     * Pushes the given {@code value} on the top of the stack.
     * @param value object that is to be added to the current collection.
     * @throws NullPointerException when {@code value} is passed as {@code null}.
     */
    public void push(Object value) {
        this.adapteeCollection.add(value);
    }

    /**
     * Removes last value pushed on stack from stack.
     *
     * @throws EmptyStackExceptionV2 when the stack is empty.
     * @return {@link Object} instance removed from the top of the stack.
     */
    public Object pop() {
        if (this.isEmpty()) {
            throw new EmptyStackExceptionV2("Cannot pop when the stack is empty!");
        }
        Object poppedObject = this.peek();
        this.adapteeCollection.remove(this.size() - 1);

        return poppedObject;
    }

    /**
     * Provides the last value pushed on stack from stack, without removing it.
     *
     * @throws EmptyStackExceptionV2 when the stack is empty.
     * @return {@link Object} instance that is the first value on the top of the stack.
     */
    public Object peek() {
        if (this.isEmpty()) {
            throw new EmptyStackExceptionV2("Cannot peek when the stack is empty!");
        }
        return this.adapteeCollection.get(this.size() - 1);
    }

    /**
     * Removes all elements from stack.
     */
    public void clear() {
        this.adapteeCollection.clear();
    }
}
