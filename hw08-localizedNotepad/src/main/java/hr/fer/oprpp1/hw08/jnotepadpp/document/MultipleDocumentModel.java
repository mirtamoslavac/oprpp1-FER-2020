package hr.fer.oprpp1.hw08.jnotepadpp.document;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The {@code MultipleDocumentModel} interface represents a model capable of containing zero, one or more documents.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * Creates a new text editor with an empty document and assigns it a new {@link SingleDocumentModel} instance within the current multiple document model.
     *
     * @return new {@link SingleDocumentModel} instance for the new document.
     */
    SingleDocumentModel createNewDocument();

    /**
     * Retrieves the currently active single document model.
     *
     * @return the currently active {@link SingleDocumentModel} instance.
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a new text editor for a document at the given {@code path} and assigns it a new {@link SingleDocumentModel} instance within the current multiple document model.
     *
     * @param path the path from which to load the document that is to be edited.
     * @throws NullPointerException when the given {@code path} is {@code null}.
     * @return {@link SingleDocumentModel} instance, i.e. the newly opened text editor.
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves the given document {@code model} to either the given {@code newPath} or the associated file path with that {@code model}, if {@code newPath} is {@code null}.
     *
     * @param model the modified single document model that is to be saved.
     * @param newPath the path to which the document is to be saved.
     * @throws NullPointerException when the given {@code model} is {@code null}.
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes and removes the given {@code model} from the current multiple document model.
     *
     * @param model the single document model that is to be closed.
     * @throws NullPointerException when the given {@code model} is {@code null}.
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Registers a new listener on the current multiple document model.
     *
     * @param l the listener to be registered.
     * @throws NullPointerException when the given {@code l} is {@code null}.
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Unregisters a specific listener {@code l} from the current multiple document model.
     *
     * @param l the listener to be unregistered.
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Determines the number of open single document models at a given moment.
     *
     * @return number of open text editors.
     */
    int getNumberOfDocuments();

    /**
     * Retrieves the ({@code index}+1)-th {@link SingleDocumentModel} instance within the current multiple document model.
     *
     * @throws IndexOutOfBoundsException if the given {@code index} is out of bounds for the given collection of currently open document models.
     * @param index index of the wanted document model.
     * @return specified {@link SingleDocumentModel} instance.
     */
    SingleDocumentModel getDocument(int index);
}
