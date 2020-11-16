package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * The {@code StudentDB} class is a command-line executor of multiple queries on an already defined database.
 *
 * @author mirtamoslavac
 * @version 1.1
 */
public class StudentDB {
    /**
     * String that terminates the program.
     */
    private static final String TERMINATOR = "exit";

    /**
     * String that represents the start of a query.
     */
    private static final String QUERY_TAG = "query";

    /**
     * Program used to execute queries on a database.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        List<String> rows = null;

        try {
             rows = Files.readAllLines(
                    Paths.get("./database.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        StudentDatabase db = new StudentDatabase(rows);

        Scanner sc = new Scanner(System.in);

            while(true) {
                try {
                System.out.print("> ");
                String next = sc.nextLine().trim();

                if(next.equalsIgnoreCase(TERMINATOR)) {
                    System.out.println("Goodbye!");
                    break;
                }
                if(next.isBlank()) throw new IllegalArgumentException("The query input cannot be blank!");

                String[] separated = next.split("\\s+", 2);
                if (separated.length != 2) throw new IllegalArgumentException("Invalid query formatting!");
                if (!separated[0].equalsIgnoreCase(QUERY_TAG)) throw new IllegalArgumentException("Invalid query invocation!");

                List<StudentRecord> resultRows = getRelevantRecords(db, separated[1]);

                List<String> output = new RecordFormatter().format(resultRows);
                output.forEach(System.out::println);
                } catch (RuntimeException e) {
                    System.out.println(e.getClass().getCanonicalName() + "   " +e.getMessage() + "\n");
                }
            }

            sc.close();

    }

    /**
     * Retrieves all {@link StudentRecord} instances relevant to the currently processed query.
     *
     * @param db database with all student records.
     * @param query currently processed query.
     * @throws NullPointerException when the given {@code db} ot {@code query} are {@code null}.
     * @return list of all records related to the query.
     */
    private static List<StudentRecord> getRelevantRecords(StudentDatabase db, String query) {
        Objects.requireNonNull(db, "The given database cannot be null!");
        QueryParser parser = new QueryParser(Objects.requireNonNull(query, "The given query cannot be null!"));
        List<StudentRecord> studentRecords;

        if (parser.isDirectQuery()) {
            studentRecords = new ArrayList<>(1);

            StudentRecord fetchedStudent = db.forJMBAG(parser.getQueriedJMBAG());
            if (fetchedStudent != null) studentRecords.add(fetchedStudent);
            return studentRecords;
        }

        return db.filter(new QueryFilter(parser.getQuery()));
    }
}
