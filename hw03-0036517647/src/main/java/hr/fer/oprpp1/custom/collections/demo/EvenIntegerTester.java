package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

/**
 * {@code EvenIntegerTester} is an implementation of {@link Tester} used to demonstrate its capability.
 */
public class EvenIntegerTester implements Tester<Object> {

    /**
     * {@inheritDoc}
     * Method taken from this homework's PDF dedicated to demonstrating one of many possible {@link Tester} implementations.
     */
    @Override
    public boolean test(Object obj) {
        if (!(obj instanceof Integer)) return false;
        Integer i = (Integer) obj;
        return i % 2 == 0;
    }
}

