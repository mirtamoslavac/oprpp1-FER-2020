package hr.fer.oprpp1.hw04.db;

/**
 * The {@code FieldValueGetters} class defines {@link IFieldValueGetter} instances that are used to obtain the requested field value.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class FieldValueGetters {
    /**
     * First name getter.
     */
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

    /**
     * Last name getter.
     */
    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

    /**
     * JMBAG getter.
     */
    public static final IFieldValueGetter JMBAG = record -> String.valueOf(record.getJmbag());

}
