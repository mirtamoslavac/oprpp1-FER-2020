package hr.fer.oprpp1.hw04.db;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code StudentDatabase} class represents a model of a database that stores {@link StudentRecord} instances.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class StudentDatabase {
    /**
     * Internal list of student records in the current database.
     */
    private final List<StudentRecord> studentRecords;

    /**
     * Map used as an index for fast retrieval of student records when a JMBAG is the comparison operator in the current database.
     */
    private final Map<String, StudentRecord> indexByJMBAG;

    /**
     * Creates a new {@code StudentDatabase} instance with all valid rows which represent {@link StudentRecord} instances.
     *
     * @param fileRows list containing the entire database stored row by row.
     * @throws NullPointerException when the given {@code filesRows} list is null.
     */
    public StudentDatabase(List<String> fileRows) {
        Objects.requireNonNull(fileRows, "There should be at least one student record in the database!");

        this.studentRecords = new ArrayList<>(fileRows.size());
        this.indexByJMBAG = new HashMap<>(fileRows.size());

        for (String row : fileRows) {
            StudentRecord studentRecord = this.parseStudentRecord(row);

            StudentRecord returnedStudentRecord = this.indexByJMBAG.putIfAbsent(studentRecord.getJmbag(), studentRecord);
            if (returnedStudentRecord != null) throw new IllegalArgumentException("The given JMBAG already exists in the database!");

            this.studentRecords.add(studentRecord);
        }

    }

    /**
     * Obtains requested record using the JMBAG index in O(1).
     *
     * @param jmbag the JMBAG of the wanted record.
     * @return requested student record if it exists, {@code null} otherwise.
     */
    public StudentRecord forJMBAG(String jmbag) {
        return this.indexByJMBAG.get(jmbag);
    }

    /**
     * Filters while looping through all student records in the current DB's internal list.
     *
     * @param filter predicate by which the student records in the current database are filtered.
     * @return list of student records for which the {@link IFilter#accepts} method returns {@code true}.
     */
    public List<StudentRecord> filter(IFilter filter) {
        return this.studentRecords.stream().filter(filter::accepts).collect(Collectors.toList());
    }

    /**
     * Parses a single student record.
     *
     * @param row String representation of a student record.
     * @throws IllegalStateException when the number of elements within the record is not correct or when the given grade is not within a specified range.
     * @return new {@link StudentRecord} instance.
     */
    private StudentRecord parseStudentRecord(String row) {
        String[] elements = row.split("\\t");

        if (elements.length != 4) throw new IllegalArgumentException("The number of elements in the database row is not correct!" + elements.length);

        int finalGrade;
        try {
            finalGrade = Integer.parseInt(elements[3]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
        if (finalGrade < 1 || finalGrade > 5) throw new IllegalArgumentException("The given grade is not within the range from 1 to 5!");

        return new StudentRecord(elements[0], elements[1], elements[2], finalGrade);
    }
}
