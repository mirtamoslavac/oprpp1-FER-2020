package hr.fer.oprpp1.hw08.jnotepadpp.document;

/**
 * The {@code MultipleDocumentListener} models a listener that observes and notifies the interested entities about changes within a respective {@link MultipleDocumentModel} instance.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface MultipleDocumentListener {
    /**
     * Notifies the interested entities about the change of the currently active document model, i.e. the text editor in focus.
     *
     *
     * @throws IllegalArgumentException when both {@code previousModel} and {@code currentModel} are {@code null}.
     * @param previousModel the previously active document model, i.e. text editor. {@code null} when the first and single document model is opened.
     * @param currentModel the newly active document model, i.e. text editor, after the change. {@code null} then the last previously opened document model is closed.
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Notifies the interested entities about a new document model that was opened.
     *
     * @throws NullPointerException when the given {@code model} is {@code null}.
     * @param model the newly opened document model, i.e. text editor.
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Notifies the interested entities about a previously opened document model that was closed.
     *
     * @param model the closed document model, i.e. text editor.
     */
    void documentRemoved(SingleDocumentModel model);

}
