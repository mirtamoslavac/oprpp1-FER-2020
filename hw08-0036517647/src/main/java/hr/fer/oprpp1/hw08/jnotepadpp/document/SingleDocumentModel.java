package hr.fer.oprpp1.hw08.jnotepadpp.document;

import javax.swing.*;
import java.nio.file.Path;

/**
 * The {@code SingleDocumentModel} interface represents a model of a single document.
 * It contains information about its file path, modification status and respective reference to Swing editor component.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface SingleDocumentModel {
    /**
     * Fetches the text component of the current document model.
     *
     * @return {@link JTextArea} instance of this model.
     */
    JTextArea getTextComponent();

    /**
     * Fetches the file path of the current document model.
     * It can be {@code null} for a new document that has not yet been saved.
     *
     * @return {@link Path} instance of this model.
     */
    Path getFilePath();

    /**
     * Sets a new path of the current document model to the given {@code path}.
     *
     * @throws NullPointerException when the given {@code path} is {@code null}.
     * @param path the path that is to be set.
     */
    void setFilePath(Path path);

    /**
     * Determines whether the current document model was modified after the last save.
     *
     * @return {@code true} if the model contains new modifications, {@code false} otherwise.
     */
    boolean isModified();

    /**
     * Updates the modification state of the current document model.
     *
     * @param modified the new modification state.
     */
    void setModified(boolean modified);

    /**
     * Registers a new listener on the current document model.
     *
     * @throws NullPointerException when the given {@code l} is {@code null}.
     * @param l the listener to be registered.
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Unregisters a specific listener {@code l} from the current document model.
     *
     * @param l the listener to be unregistered.
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}
