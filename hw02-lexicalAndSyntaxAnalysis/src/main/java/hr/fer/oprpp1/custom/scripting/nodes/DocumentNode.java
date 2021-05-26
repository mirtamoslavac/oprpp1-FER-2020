package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * The {@code DocumentNode} class represents an entire document.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class DocumentNode extends Node {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, numberOfChildren = this.numberOfChildren(); i < numberOfChildren ; i++) {
            Node childNode = this.getChild(i);
            sb.append(childNode.toString());
        }

        return sb.toString();
    }
}
