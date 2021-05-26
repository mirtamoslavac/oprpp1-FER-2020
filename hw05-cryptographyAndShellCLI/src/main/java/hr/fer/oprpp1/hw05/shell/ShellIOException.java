package hr.fer.oprpp1.hw05.shell;

/**
 * Thrown to indicate that reading or writing within the shell environment failed.
 *
 * @author  mirtamoslavac
 * @version 1.0
 */
public class ShellIOException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 1599513577532486842L;

    /**
     * Constructs an {@code ShellIOException} with no detail message.
     */
    public ShellIOException() {
        super();
    }

    /**
     * Constructs an {@code ShellIOException} with the specified detail message.
     *
     * @param message the detail message
     */
    public ShellIOException(String message) {
        super(message);
    }

}
