package hr.fer.oprpp1.hw04.db;

import hr.fer.oprpp1.hw04.db.lexer.QueryLexer;
import hr.fer.oprpp1.hw04.db.lexer.QueryLexerException;
import hr.fer.oprpp1.hw04.db.lexer.QueryToken;
import hr.fer.oprpp1.hw04.db.lexer.QueryTokenType;

import java.util.*;

import static java.util.Map.entry;

/**
 * The {@code QueryParser} class represents a parser of query statement.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class QueryParser {
    /**
     * {@link QueryLexer} instance used by the current parser to tokenize characters.
     */
    private final QueryLexer lexer;

    /**
     * List of {@link ConditionalExpression} instances contained within the entry.
     */
    private final List<ConditionalExpression> expressions;

    /**
     * The state of the currently parsed query.
     */
    private boolean directQuery;

    /**
     * Conjunction between expressions.
     */
    private static final String CONJUNCTION = "AND";

    /**
     * Map of valid attributes with their respective {@link FieldValueGetters} instances.
     */
    private final Map<String, IFieldValueGetter> validAttributesWithGetters = Map.ofEntries(
            entry("jmbag", FieldValueGetters.JMBAG),
            entry("lastname", FieldValueGetters.LAST_NAME),
            entry("firstname", FieldValueGetters.FIRST_NAME)
    );

    /**
     * Map of valid operators with their respective {@link ComparisonOperators} instances.
     */
    private final Map<String, IComparisonOperator> validOperators = Map.ofEntries(
            entry("<", ComparisonOperators.LESS),
            entry("<=", ComparisonOperators.LESS_OR_EQUALS),
            entry(">", ComparisonOperators.GREATER),
            entry(">=", ComparisonOperators.GREATER_OR_EQUALS),
            entry("!=", ComparisonOperators.NOT_EQUALS),
            entry("=", ComparisonOperators.EQUALS),
            entry("LIKE", ComparisonOperators.LIKE)
    );

    /**
     * Creates a new {@code QueryParser} instance.
     * It also creates its own new {@link QueryLexer} instance which enables it to define tokens of the current query.
     *
     * @param query input text for the current parser.
     * @throws NullPointerException when the given {@code query} is {@code null}.
     * @throws QueryParserException when the input text cannot be parsed due to an error that occurred during the parse.
     */
    public QueryParser(String query) {
        this.lexer = new QueryLexer(Objects.requireNonNull(query, "The given query cannot be null!"));
        this.directQuery = false;
        this.expressions = new ArrayList<>();

        try {
            this.parseQuery();
        } catch (RuntimeException e) {
            throw new QueryParserException(e.getMessage());
        }
    }

    /**
     * Parses the entire query, with a variable amount of expressions.
     *
     * @throws QueryParserException when the current parser cannot properly process the given query.
     */
    private void parseQuery() {
        this.lexer.nextToken();
        this.expressions.add(parseSingleExpression());
        boolean consumedConjunction = false;
        while (true) {
            QueryToken nextToken = this.lexer.nextToken();
            if (nextToken.getType() == QueryTokenType.EOF) break;
            if (nextToken.getType() == QueryTokenType.FIELD_VALUE && nextToken.getValue().toString().equalsIgnoreCase(CONJUNCTION)) {
                consumedConjunction = true;
                continue;
            }
            if (nextToken.getType() == null || nextToken.getType() != QueryTokenType.FIELD_VALUE || nextToken.getType() == QueryTokenType.FIELD_VALUE && !consumedConjunction) throw new QueryParserException("Missing or incorrect conjunction!");
            this.expressions.add(parseSingleExpression());
            consumedConjunction = false;
        }

        if (expressions.size() == 1 && expressions.get(0).getFieldValueGetter() == FieldValueGetters.JMBAG && expressions.get(0).getComparisonOperator() == ComparisonOperators.EQUALS) this.directQuery = true;
    }

    /**
     * Parses a single expression within the query.
     *
     * @throws QueryParserException when the current parser cannot properly process the given query.
     * @return new {@link ConditionalExpression} instance for the current expression.
     */
    private ConditionalExpression parseSingleExpression() {
        QueryToken attribute = this.lexer.getToken();
        if (attribute.getType() != QueryTokenType.FIELD_VALUE) throw new QueryParserException("Expected field_value type, got " + attribute.getType().toString().toLowerCase() +
                "!");
        if (attribute.getValue().toString().equalsIgnoreCase("finalgrade")) throw new QueryParserException("Cannot query by finalGrade!");
        IFieldValueGetter attributeGetter = this.validAttributesWithGetters.get(attribute.getValue().toString().toLowerCase());
        if (attributeGetter == null)
            throw new QueryParserException("Expected a name of a valid attribute, got \"" + attribute.getValue().toString() + "\"!");

        QueryToken operator = this.lexer.nextToken();
        if (operator.getType() != QueryTokenType.OPERATOR) throw new QueryParserException("Expected operator type, got \"" + operator.getType().toString().toLowerCase() + "\"!");
        IComparisonOperator comparisonOperator = this.validOperators.get(operator.getValue().toString().toUpperCase());
        if (comparisonOperator == null) throw new QueryParserException("Expected a valid operator, got \"" + operator.getValue().toString() + "\"!");

        QueryToken stringLiteral = this.lexer.nextToken();
        if (stringLiteral.getType() != QueryTokenType.STRING) throw new QueryParserException("Expected string (surrounded by quotation marks), got " + stringLiteral.getType().toString().toLowerCase() + "!");

        return new ConditionalExpression(attributeGetter, stringLiteral.getValue().toString(), comparisonOperator);
    }

    /**
     * Determines whether the query has only one comparison in the form of {@literal jmbag="xxx"}, aka is classified as direct query.
     *
     * @return {@code true} if the query direct, {@code false} otherwise.
     */
    public boolean isDirectQuery() {
        return this.directQuery;
    }

    /**
     * Retrieves the corresponding JMBAG to the current direct query.
     *
     * @return JMBAG of the current query.
     * @throws IllegalStateException when the query in question is not a direct one.
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery()) throw new IllegalStateException("The JMBAG cannot be retrieved from a non direct query!");
        return expressions.get(0).getStringLiteral();
    }

    /**
     * Returns all expressions for all query types.
     *
     * @return list of conditional expressions from the given query.
     */
    public List<ConditionalExpression> getQuery() {
        return this.expressions;
    }
}
