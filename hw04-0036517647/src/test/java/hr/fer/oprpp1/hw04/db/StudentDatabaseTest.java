package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDatabaseTest {

    private StudentDatabase database;
    private final IFilter acceptAll = record -> true;
    private final IFilter declineAll = record -> false;

    @BeforeEach
    void setUp() throws IOException {
       this.database = new StudentDatabase(Files.readAllLines(
                Paths.get("src/main/resources/database.txt"),
                StandardCharsets.UTF_8));
    }

    @Test
    void testForCorrectJMBAG() {
        StudentRecord record = this.database.forJMBAG("0000000031");

        assertEquals("0000000031", record.getJmbag());
        assertEquals("Krušelj Posavec", record.getLastName());
        assertEquals("Bojan", record.getFirstName());
        assertEquals(4, record.getFinalGrade());
    }

    @Test
    void testForCorrectJMBAGIncorrectData() {
        StudentRecord record = this.database.forJMBAG("0000000001");

        assertNotEquals("0000000031", record.getJmbag());
        assertNotEquals("Krušelj Posavec", record.getLastName());
        assertNotEquals("Bojan", record.getFirstName());
        assertNotEquals(4, record.getFinalGrade());
    }

    @Test
    void testForIncorrectJMBAG() {
        assertNull(this.database.forJMBAG("0123456789"));
    }

    @Test
    void testForNullJMBAG() {
        assertNull(this.database.forJMBAG(null));
    }

    @Test
    void testFilterReturnsAll() {
        assertEquals(63, this.database.filter(this.acceptAll).size());
    }

    @Test
    void testFilterReturnsNone() {
        assertTrue(this.database.filter(this.declineAll).isEmpty());
    }

}
