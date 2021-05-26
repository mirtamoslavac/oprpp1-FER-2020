package hr.fer.oprpp1.hw08.jnotepadpp.document;

/**
 * The {@code SingleDocumentListener} models a listener that observes and notifies the interested entities about changes within a respective {@link SingleDocumentModel} instance.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface SingleDocumentListener {
    /**
     * Notifies the interested entities about the {@code model}'s status update.
     *
     * @param model the updated instance.
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Notifies the interested entities about the {@code model}'s file path update.
     *
     * @param model the updated instance.
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
