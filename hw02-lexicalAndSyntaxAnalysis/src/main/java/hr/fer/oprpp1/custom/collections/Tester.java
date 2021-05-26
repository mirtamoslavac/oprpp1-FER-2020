package hr.fer.oprpp1.custom.collections;

/**
 * The {@code Tester} interface models an object that determines whether the other object is acceptable.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface Tester {

    /**
     * Tests whether the given {@code object} is accepted, in accordance with the specification of the current {@code Tester} instance.
     *
     * @param obj object whose acceptance is tested.
     * @return {@code true} if the {@code object} is accepted, {@code false} otherwise.
     */
    boolean test(Object obj);
}
