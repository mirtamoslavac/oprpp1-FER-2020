package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

import java.util.Objects;

/**
 * The {@code Node} abstract class is used as a base class for representing graph nodes/structured documents.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public abstract class Node {

    /**
     * Collection of all children of the current instance.
     */
    private ArrayIndexedCollection children;

    /**
     * Creates a new {@code Node} instance.
     */
    public Node() {
        this.children = null;
    }

    /**
     * Adds given child to an internally managed collection of children.
     *
     * @param child the given {@link Node} instance to be added.
     */
    public void addChildNode(Node child) {
        if (this.children == null) this.children = new ArrayIndexedCollection();
        this.children.add(child);
    }

    /**
     * Returns a number of (direct) children.
     *
     * @return number of children.
     */
    public int numberOfChildren() {
        if (this.children == null) return 0;
        return this.children.size();
    }

    /**
     * Returns selected child.
     *
     * @throws IndexOutOfBoundsException when the node does not have any children yet or the {@code index} is not within the range from 0 to {@code this.numberOfChildren}.
     */
    public Node getChild(int index) {
        if (this.children == null) throw new IndexOutOfBoundsException("Cannot get child if the node has no children yet!");
        if (index < 0 || index >= this.numberOfChildren()) throw new IndexOutOfBoundsException("Invalid number of wanted child!");

        return (Node)this.children.get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Objects.equals(this.children, node.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.children);
    }
}
