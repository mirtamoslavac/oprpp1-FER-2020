package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {

    private QueryParser queryParser;

    private final String directQuery = "jmbag = \"0000000032\"";
    private final String multipleQuery = "jmbag = \"0000000032\" AND firstName = \"Maja\"";
    private final String directQueryWhitespace = "        jmbag=              \"0000000032\"";
    private final String directQueryUpperLowerCase = "JmbAg =\"0000000032\"";
    private final String queryNoConjunction = "firstName = \"Maja\" jmbag = \"1235467890\"";
    private final String queryWrongConjunction = "firstName = \"Maja\" UND jmbag = \"1235467890\"";
    private final String queryNoAttributeName = "= \"0000000032\"";
    private final String queryInvalidAttributeName = "name > \"Maja\"";
    private final String queryByFinalGrade = "finalGrade < 5";
    private final String queryNoOperator = "firstName IS Maja";
    private final String queryInvalidOperator = "jmbag % \"145\"";
    private final String queryNoStringLiteral = "firstName = Maja";
    private final String multipleQueryWhitespaceAndUpperLowerCase = "        jmbag=              \"0000000032\"  AND firstnaME     LIke \"B*\"";

    @Test
    void testQueryParserPassNullThrows() {
        assertThrows(NullPointerException.class, () -> new QueryParser(null));
    }

    @Test
    void testQueryDirectQuery() {
    }

    @Test
    void testQueryIsDirectQuery() {
        this.queryParser = new QueryParser(this.directQuery);
        assertTrue(this.queryParser.isDirectQuery());
    }

    @Test
    void testQueryIsNotDirectQuery() {
        this.queryParser = new QueryParser(this.multipleQuery);
        assertFalse(this.queryParser.isDirectQuery());
    }

    @Test
    void testQueryGetQueriedJMBAGDirectQuery() {
        assertEquals("0000000032", new QueryParser(this.directQuery).getQueriedJMBAG());
    }

    @Test
    void testQueryGetQueriedJMBAGNotDirectQueryThrows() {
        assertThrows(IllegalStateException.class, () -> new QueryParser(this.multipleQuery).getQueriedJMBAG());
    }

    @Test
    void testQueryDirectQueryWithWhitespace() {
        this.queryParser = new QueryParser(this.directQueryWhitespace);
        assertTrue(this.queryParser.isDirectQuery());
    }

    @Test
    void testQueryDirectQueryUpperLowerCase() {
        this.queryParser = new QueryParser(this.directQueryUpperLowerCase);
        assertTrue(this.queryParser.isDirectQuery());
    }

    @Test
    void testQueryMissingConjunctionThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryNoConjunction));
    }

    @Test
    void testQueryWrongConjunctionThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryWrongConjunction));
    }

    @Test
    void testQueryMissingColumnNameThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryNoAttributeName));
    }

    @Test
    void testQueryWrongColumnNameThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryInvalidAttributeName));
    }

    @Test
    void testQueryByFinalGrade() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryByFinalGrade));
    }

    @Test
    void testQueryMissingOperatorThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryNoOperator));
    }

    @Test
    void testQueryWrongOperatorThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryInvalidOperator));
    }

    @Test
    void testQueryMissingStringLiteralThrows() {
        assertThrows(QueryParserException.class, () -> new QueryParser(this.queryNoStringLiteral));
    }

    @Test
    void testMultipleQueryWithWhitespaceAndUpperLowerCase() {
        this.queryParser = new QueryParser(this.multipleQueryWhitespaceAndUpperLowerCase);
        assertFalse(this.queryParser.isDirectQuery());
    }

}
