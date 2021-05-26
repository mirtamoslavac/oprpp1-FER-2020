package hr.fer.oprpp1.hw08.jnotepadpp.document.implementation;

import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The {@code DefaultSingleDocumentModel} class represents an implementation of the {@link SingleDocumentModel}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
    /**
     * The path of the current document.
     */
    private Path filePath;
    /**
     * Component used for text editing of the current document.
     */
    private final JTextArea textComponent;
    /**
     * Flag for the modification state of the current default model.
     */
    private boolean modified;
    /**
     * The text of the currently opened document, before any modification within the text editor or after being saved.
     */
    private String originalText;
    /**
     * Listeners registered on the current default model.
     */
    private final List<SingleDocumentListener> listeners;

    /**
     * Creates a new {@code DefaultSingleDocumentModel} instance with the given {@code filePath} and {@code textContent}.
     *
     * @param filePath the path of the current model.
     * @param textContent the text initially shown within the editor.
     */
    public DefaultSingleDocumentModel(Path filePath, String textContent) {
        this.filePath = filePath != null ? filePath.toAbsolutePath().normalize() : null;
        this.textComponent = new JTextArea(textContent != null ? textContent : "");
        this.listeners = new ArrayList<>();
        this.originalText = textContent;

        this.textComponent.getDocument().addDocumentListener(new DocumentListener() {
            private final Supplier<Boolean> currentlyModified = () -> !originalText.equals(textComponent.getText());

            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(this.currentlyModified.get());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(this.currentlyModified.get());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(this.currentlyModified.get());
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return this.textComponent;
    }

    @Override
    public Path getFilePath() {
        return this.filePath;
    }

    @Override
    public void setFilePath(Path path) {
        this.filePath = path.toAbsolutePath().normalize();
        this.notifyRegisteredListeners(listener -> listener.documentFilePathUpdated(this));
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        if (!modified) this.originalText = this.textComponent.getText();

        this.notifyRegisteredListeners(listener -> listener.documentModifyStatusUpdated(this));
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        this.listeners.add(Objects.requireNonNull(l, "The given listener that is to be added cannot be null!"));
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        this.listeners.remove(l);
    }

    /**
     * Notifies all listeners within the {@code listeners}, registered for receiving events from the current model.
     *
     * @param action action that is performed for every registered listener.
     */
    private void notifyRegisteredListeners(Consumer<SingleDocumentListener> action) {
        this.listeners.forEach(action);
    }
}