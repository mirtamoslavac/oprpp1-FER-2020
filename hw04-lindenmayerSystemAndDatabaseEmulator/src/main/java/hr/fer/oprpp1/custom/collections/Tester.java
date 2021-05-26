package hr.fer.oprpp1.custom.collections;

/**
 * The {@code Tester} interface models an object that determines whether the other object is acceptable.
 *
 * @param <T> type of object that is to be tested.
 * @author mirtamoslavac
 * @version 2.0
 */
@FunctionalInterface
public interface Tester<T> {

    /**
     * Tests whether the given {@code object} is accepted, in accordance with the specification of the current {@code Tester} instance.
     *
     * @param obj object whose acceptance is tested.
     * @return {@code true} if the {@code object} is accepted, {@code false} otherwise.
     */
    boolean test(T obj);
}
