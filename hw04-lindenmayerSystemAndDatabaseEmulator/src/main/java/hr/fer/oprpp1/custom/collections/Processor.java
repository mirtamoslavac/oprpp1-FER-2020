package hr.fer.oprpp1.custom.collections;

/**
 * The {@code Processor} interface models an object capable of performing some operation on the passed object.
 *
 * @param <T> type of object that  is to be processed.
 * @author mirtamoslavac
 * @version 2.0
 */
@FunctionalInterface
public interface Processor<T> {

    /**
     * Enables performing some operation on the passed object that can later be overridden and implemented.
     *
     * @param value object upon which some operation is to be performed.
     */
    void process(T value);

}
