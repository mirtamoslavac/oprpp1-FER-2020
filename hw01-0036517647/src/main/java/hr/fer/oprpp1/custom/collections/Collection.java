package hr.fer.oprpp1.custom.collections;

/**
 * The {@code Collection} class represents a general collection of objects.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Collection {

    /**
     * Default constructor.
     */
    protected Collection() {

    }

    /**
     * Determines whether the current collection is empty.
     *
     * @return {@code true} if the current collection contains no objects, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Determines the size of the current collection, also known as the number of elements within it.
     *
     * @return the number of currently stored objects in the current collection.
     */
    public int size(){
        return 0;
    }

    /**
     * Adds the given object into the current collection.
     *
     * @param value object that is to be added to this collection.
     */
    public void add(Object value) {

    }

    /**
     * Determines whether this current collection contains the given object.
     *
     * @param value object that is to be checked whether it exists in the current collection, can be passed as null.
     * @return {@code true} only if the collection contains given value, {@code false} otherwise.
     */
    public boolean contains(Object value){
        return false;
    }

    /**
     * Removes one occurrence of the given object, that exists within the current collection, from it.
     *
     * @param value object whose single occurrence (of many possible) is to be removed from this collection.
     * @return {@code true} only if the collection contains the given value and removes it, {@code false} if otherwise
     */
    public boolean remove(Object value){
        return false;
    }

    /**
     * Allocates a new array with the size that equals to the size of this collection, fills it with collection content and
     * returns the array.
     *
     * @throws UnsupportedOperationException when attempting to return an unallocated array.
     * @return array filled with objects contained within the current collection.
     */
    public Object[] toArray() {
        Object[] objectArray = null;

        if (objectArray == null){
            throw new UnsupportedOperationException("The toArray method should never return null!");
        }

        return objectArray;
    }

    /**
     * Calls {@link Processor#process(Object)} of for each element of this collection.
     *
     * @param processor processor that performs a process for every object within the current collection.
     */
    public void forEach(Processor processor) {

    }

    /**
     * Adds all elements from the given collection into the current collection.
     * The given collection remains unchanged.
     *
     * @param other the given collection whose elements are to be added to the current collection.
     * @throws NullPointerException when {@code other}(the given collection) is passed as {@code null}.
     */
    public void addAll(Collection other) {

        if (other == null) throw new NullPointerException("The given collection should not be null.");

        /**
         * {@code AddAllElementsProcessor} is a local class that is used to add all elements of the given collection into the current collection.
         */
        class AddAllElementsProcessor extends Processor {

            /**
             * Adds the given object to the current collection.
             *
             * @param value object upon which some operation is to be performed.
             */
            @Override
            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new AddAllElementsProcessor());
    }

    /**
     * Removes all elements from the current collection.
     */
    public void clear() {

    }

}
