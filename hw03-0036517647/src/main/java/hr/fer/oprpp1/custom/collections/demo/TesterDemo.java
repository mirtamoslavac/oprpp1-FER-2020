package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

/**
 * {@code Tester} is a command-line application that demonstrates the capability of {@link EvenIntegerTester}.
 */
public class TesterDemo {

    /**
     * Method taken from this homework's PDF dedicated to demonstrating {@link EvenIntegerTester} capability.
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        Tester<Object> t = new EvenIntegerTester();
        System.out.println(t.test("Ivo"));
        System.out.println(t.test(22));
        System.out.println(t.test(3));
    }
}
