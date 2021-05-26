package hr.fer.oprpp1.hw04.db;

/**
 * The {@code IFieldValueGetter} interface represents a strategy responsible for obtaining the requested field value from the given {@link StudentRecord} instance.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
@FunctionalInterface
public interface IFieldValueGetter {

    /**
     * Fetches the requested field value from the given {@code record}.
     *
     * @param record the student record whose field value is to be obtained.
     * @return requested field value of the given {@code record}.
     */
    String get(StudentRecord record);
}
