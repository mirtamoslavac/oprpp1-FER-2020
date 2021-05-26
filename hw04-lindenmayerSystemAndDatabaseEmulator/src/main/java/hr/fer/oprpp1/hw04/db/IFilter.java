package hr.fer.oprpp1.hw04.db;

/**
 * The {@code IFilter} interface represents a model for filtering of {@link StudentRecord} instances within the {@link StudentDatabase}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
@FunctionalInterface
public interface IFilter {

    /**
     * Determines whether the given {@link StudentRecord} instance is accepted by the implemented filter.
     *
     * @param record the record to be filtered.
     * @return {@code true} if the record is accepted, {@code false} otherwise.
     */
    boolean accepts(StudentRecord record);
}
