package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * The {@code StudentRecord} represents a single student with corresponding information in the database.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class StudentRecord {
    /**
     * Student's unique identifier.
     */
    private final String jmbag;

    /**
     * Student's last name.
     */
    private final String lastName;

    /**
     * Student's first name.
     */
    private final String firstName;

    /**
     * Student's final grade.
     */
    private final int finalGrade;

    /**
     * Creates a new {@code StudentRecord} instance with all necessary information related to the student.
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        this.jmbag = Objects.requireNonNull(jmbag, "The given JMBAG cannot be null!");
        this.lastName =  Objects.requireNonNull(lastName, "The given last name cannot be null!");
        this.firstName =  Objects.requireNonNull(firstName, "The given first name cannot be null!");
        this.finalGrade =  finalGrade;
    }

    /**
     * Retrieves the current student's JMBAG, a unique identifier.
     *
     * @return JMBAG of the current student.
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Retrieves the current student's last name.
     *
     * @return last name of the current student.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the current student's first name.
     *
     * @return first name of the current student.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the current student's final grade.
     *
     * @return the final grade of the current student.
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentRecord)) return false;
        StudentRecord that = (StudentRecord) o;
        return this.jmbag.equals(that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jmbag);
    }
}
