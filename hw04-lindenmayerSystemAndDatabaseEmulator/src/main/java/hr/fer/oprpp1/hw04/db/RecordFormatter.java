package hr.fer.oprpp1.hw04.db;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * The {@code RecordFormatter} class represents a formatter for the query output.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class RecordFormatter {
    /**
     * Formats the incoming rows.
     *
     * @param resultRows {@link StudentRecord} instances that are to be formatted.
     * @return list of formatted query output.
     */
    public List<String> format(List<StudentRecord> resultRows) {
        List<String> output = new ArrayList<>(resultRows.size() + 2);

        if (Objects.requireNonNull(resultRows, "Selected records list cannot be null!").size() != 0) {
            int maxJMBAG = this.calculateMax(resultRows, r -> r.getJmbag().length());
            int maxLastName = this.calculateMax(resultRows, r -> r.getLastName().length());
            int maxFirstName = this.calculateMax(resultRows, r -> r.getFirstName().length());

            String border = "+" + "=".repeat(maxJMBAG + 2)
                    + "+" + "=".repeat(maxLastName + 2)
                    + "+" + "=".repeat(maxFirstName + 2)
                    + "+===+";

            output.add(border);

            for (StudentRecord studentRecord : resultRows) {
                int nrOfSpacesJMBAG = maxJMBAG - studentRecord.getJmbag().length() + 1;
                int nrOfSpacesLastName = maxLastName - studentRecord.getLastName().length() + 1;
                int nrOfSpacesFirstName = maxFirstName - studentRecord.getFirstName().length() + 1;
                output.add(String.format("| " + studentRecord.getJmbag() + "%" + nrOfSpacesJMBAG + "s| "
                        + studentRecord.getLastName() + "%" + nrOfSpacesLastName + "s| "
                        + studentRecord.getFirstName() + "%" + nrOfSpacesFirstName + "s| "
                        + studentRecord.getFinalGrade() + " |", "", "", ""));
            }

            output.add(border);

        }

        output.add("Records selected: " + resultRows.size() + "\n");

        return output;
    }

    /**
     * Calculated the maximum string length of an attribute value.
     *
     * @param resultRows {@link StudentRecord} instances that are to be checked.
     * @param mapperToInt mapper that determines the length of a predetermined attribute for each {@link StudentRecord} instance.
     * @throws java.util.NoSuchElementException when the max cannot be calculated (is {@code null}).
     * @return maximum length for that attribute.
     */
    private int calculateMax(List<? extends StudentRecord> resultRows, ToIntFunction<? super StudentRecord> mapperToInt) {
        return resultRows.stream()
                .mapToInt(mapperToInt)
                .max()
                .orElseThrow();
    }
}
