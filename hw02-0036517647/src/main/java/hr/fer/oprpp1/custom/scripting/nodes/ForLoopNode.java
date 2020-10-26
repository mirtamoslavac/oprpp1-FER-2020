package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * The {@code ForLoopNode} class represents a single for-loop construct.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ForLoopNode extends Node {

    /**
     * {@link ElementVariable} property.
     */
    private ElementVariable variable;

    /**
     * An {@link Element} instance representing the starting expression.
     */
    private Element startExpression;

    /**
     * An {@link Element} instance representing the ending expression.
     */
    private Element endExpression;

    /**
     * An {@link Element} instance representing the step expression.
     */
    private Element stepExpression;

    /**
     * Creates a new {@code ForLoopNode} instance with respective expressions as arguments.
     *
     * @param variable {@code ElementVariable} instance representing the loop variable.
     * @param elements array containing expressions used to construct the for loop.
     * @throws IllegalArgumentException when the number of expressions is smaller than 2 or bigger than 3, or they are either an operator or a function name.
     * @throws NullPointerException when {@code variable} or either one of start and end expressions is {@code null}.
     */
    public ForLoopNode(ElementVariable variable, Element ...elements) {
        if (elements.length < 2) throw new IllegalArgumentException("Too few arguments for a for loop function!");
        if (elements.length > 3) throw new IllegalArgumentException("Too many arguments for a for loop function!");
        if (variable == null || elements[0] == null || elements[1] == null) throw new NullPointerException("Argument cannot be null!");
        for (Element element : elements) {
            if(element instanceof ElementFunction || element instanceof ElementOperator) throw new IllegalArgumentException("Invalid argument type for a for loop function!");
        }

        this.variable = variable;
        this.startExpression = elements[0];
        this.endExpression = elements[1];
        this.stepExpression = null;
        if (elements.length == 3) this.stepExpression = elements[2];
    }

    /**
     * Fetches the {@code variable} of the current instance.
     *
     * @return {@code variable}.
     */
    public ElementVariable getVariable() {
        return this.variable;
    }

    /**
     * Fetches the {@code startExpression} of the current instance.
     *
     * @return {@code startExpression}.
     */
    public Element getStartExpression() {
        return this.startExpression;
    }

    /**
     * Fetches the {@code endExpression} of the current instance.
     *
     * @return {@code endExpression}.
     */
    public Element getEndExpression() {
        return this.endExpression;
    }

    /**
     * Fetches the {@code stepExpression} of the current instance.
     *
     * @return {@code stepExpression}.
     */
    public Element getStepExpression() {
        return this.stepExpression;
    }

    /**
     * Returns the string representation of the current {@code ForLoopNode} instance with nested expressions, matching the original input without the redundant whitespace in front
     * of the tag name, as well as redundant whitespace between elements.
     *
     * @return string representation of the current {@code ForLoopNode} instance.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{$ FOR ");
        sb.append(this.variable.asText()).append(" ");
        sb.append(this.startExpression.asText()).append(" ");
        sb.append(this.endExpression.asText());
        if (this.stepExpression != null) sb.append(" ").append(this.stepExpression.asText());
        sb.append("$}");

        for (int i = 0, numberOfChildren = this.numberOfChildren(); i < numberOfChildren ; i++) {
            Node childNode = this.getChild(i);
            sb.append(childNode.toString());
        }

        sb.append("{$END$}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForLoopNode)) return false;
        ForLoopNode that = (ForLoopNode) o;
        return this.variable.equals(that.variable) &&
                this.startExpression.equals(that.startExpression) &&
                this.endExpression.equals(that.endExpression) &&
                Objects.equals(this.stepExpression, that.stepExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.variable, this.startExpression, this.endExpression, this.stepExpression);
    }
}
