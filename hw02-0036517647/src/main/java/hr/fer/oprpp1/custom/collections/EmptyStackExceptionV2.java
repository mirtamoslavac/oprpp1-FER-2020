package hr.fer.oprpp1.custom.collections;

/**
 * Thrown to indicate that one is accessing elements of the stack when it's empty.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class EmptyStackExceptionV2 extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = 1234567890987654321L;

    /**
     * Constructs an {@code EmptyStackExceptionV2} with no detail message.
     */
    public EmptyStackExceptionV2() {
    }

    /**
     * Constructs an {@code EmptyStackExceptionV2} with the specified detail message.
     *
     * @param message the detail message
     */
    public EmptyStackExceptionV2(String message) {
        super(message);
    }
}
