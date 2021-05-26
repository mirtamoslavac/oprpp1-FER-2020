package hr.fer.oprpp1.hw08.jnotepadpp.document.implementation;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * The {@code DefaultMultipleDocumentModel} class represents an implementation of the {@link MultipleDocumentModel}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
    @java.io.Serial
    private static final long serialVersionUID = 7984653122001156848L;
    /**
     * The {@link FormLocalizationProvider} instance received from the respective
     */
    private final FormLocalizationProvider flp;
    /**
     * Relative path to the icon representing that a certain document has not been modified.
     */
    private static final String NOT_MODIFIED_ICON_SOURCE = "../../icons/green_not_modified.png";
    /**
     * Relative path to the icon representing that a certain document has been modified.
     */
    private static final String MODIFIED_ICON_SOURCE = "../../icons/red_modified.png";
    /**
     * Documents currently opened within the default multiple document model.
     */
    private final List<SingleDocumentModel> openedDocuments;
    /**
     * The currently active single document model within the default multiple document model.
     */
    private SingleDocumentModel currentDocument;
    /**
     * Listeners registered on the current default multiple document model.
     */
    private final List<MultipleDocumentListener> listeners;
    /**
     * Icon shown when the document that is active within the default multiple document model has not been modified.
     */
    private final ImageIcon documentNotModifiedIcon;
    /**
     * Icon shown when the document that is active within the default multiple document model has been modified.
     */
    private final ImageIcon documentModifiedIcon;

    /**
     * Creates a new {@code DefaultMultipleDocumentModel} instance.
     *
     * @param flp a {@link FormLocalizationProvider} instance used for localization.
     */
    public DefaultMultipleDocumentModel(FormLocalizationProvider flp) {
        super();
        this.openedDocuments = new ArrayList<>();
        this.currentDocument = null;
        this.listeners = new ArrayList<>();
        this.flp = flp;

        this.documentNotModifiedIcon = this.useIcon(NOT_MODIFIED_ICON_SOURCE);
        this.documentModifiedIcon = this.useIcon(MODIFIED_ICON_SOURCE);

        this.addChangeListener(e -> {
            SingleDocumentModel previousModel = this.currentDocument;

            int newTabIndex = this.getSelectedIndex();
            this.currentDocument = newTabIndex != -1 ? this.openedDocuments.get(newTabIndex) : null;

            this.notifyRegisteredListeners(listener -> listener.currentDocumentChanged(previousModel, this.currentDocument));
        });
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel previousModel = this.currentDocument;

        this.currentDocument = new DefaultSingleDocumentModel(null, "");
        this.addDocumentToModel(true);
        this.notifyRegisteredListeners(listener -> listener.documentAdded(this.currentDocument));

        this.notifyRegisteredListeners(listener -> listener.currentDocumentChanged(previousModel, this.currentDocument));

        return this.currentDocument;
    }

    /**
     * Sets up a single document listener.
     */
    private SingleDocumentListener setUpSingleDocumentListener() {
        return new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                if (model.isModified()) setIconAt(getSelectedIndex(), documentModifiedIcon);
                else setIconAt(getSelectedIndex(), documentNotModifiedIcon);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
            }
        };
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return this.currentDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        Path newDocumentPath = Objects.requireNonNull(path, "The given path cannot be null!").toAbsolutePath().normalize();

        SingleDocumentModel previousModel = this.currentDocument;
        Optional<SingleDocumentModel> possibleModel =
                this.openedDocuments.stream().filter(s -> s != null && s.getFilePath() != null && s.getFilePath().equals(newDocumentPath)).findAny();

        if (possibleModel.isPresent()) this.currentDocument = possibleModel.get();
        else {
            byte[] documentContent;
            try {
                documentContent = Files.readAllBytes(path);
            } catch (IOException exc) {
                JNotepadPP.informUser(
                        this,
                        String.format(this.flp.getString("errorWhileReading"), newDocumentPath),
                        this.flp.getString("error"),
                        JOptionPane.ERROR_MESSAGE,
                        new String[]{this.flp.getString("ok")});
                return null;
            }

            this.currentDocument = new DefaultSingleDocumentModel(path, new String(documentContent, StandardCharsets.UTF_8));
            this.addDocumentToModel(false);
            this.notifyRegisteredListeners(listener -> listener.documentAdded(this.currentDocument));
        }

        this.currentDocument.addSingleDocumentListener(setUpSingleDocumentListener());
        this.notifyRegisteredListeners(listener -> listener.currentDocumentChanged(previousModel, this.currentDocument));

        return this.currentDocument;
    }

    /**
     * Adds a document to the current multiple document model.
     *
     * @param newTab {@code true} if the document that is to be added is a new document, {@code false} if it is an already existing one.
     */
    private void addDocumentToModel(boolean newTab) {
        this.openedDocuments.add(this.currentDocument);

        this.addTab("", new JPanel().add(new JScrollPane(this.currentDocument.getTextComponent())));
        int tabIndex = this.openedDocuments.indexOf(this.currentDocument);
        Path filePath = this.currentDocument.getFilePath();
        this.setTitleAt(tabIndex, newTab ? this.flp.getString("unnamed") : filePath.getFileName().toString());
        this.setToolTipTextAt(tabIndex, newTab ? this.flp.getString("unnamed") : filePath.toString());
        this.setIconAt(tabIndex, this.documentNotModifiedIcon);
        this.setSelectedIndex(tabIndex);

        this.currentDocument.addSingleDocumentListener(setUpSingleDocumentListener());

        this.currentDocument.getTextComponent().setCaret(new DefaultCaret() {
            @Override
            public void setSelectionVisible(boolean visible) {
                super.setSelectionVisible(true);
            }
            @Override
            public void setVisible(boolean visible) {
                super.setVisible(true);
            }
        });
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        Objects.requireNonNull(model, "The given model cannot be null!");
        if (newPath == null) {
            newPath = model.getFilePath();
        } else {
            Path newDocumentPath = newPath.toAbsolutePath().normalize();
            if (this.openedDocuments.stream().anyMatch(s -> (s.getFilePath() != null && s.getFilePath().equals(newDocumentPath)) && !s.equals(model))) {
                JNotepadPP.informUser(this,
                        this.flp.getString("alreadyOpened"),
                        this.flp.getString("warning"),
                        JOptionPane.WARNING_MESSAGE,
                        new String[]{this.flp.getString("ok")});
                return;
            }
        }

        try {
            Files.writeString(Objects.requireNonNull(newPath, "The document path cannot be null!"), model.getTextComponent().getText());
        } catch (IOException e) {
            JNotepadPP.informUser(
                    this,
                    String.format(this.flp.getString("errorWhileWriting"), newPath),
                    this.flp.getString("error"),
                    JOptionPane.ERROR_MESSAGE,
                    new String[]{this.flp.getString("ok")});
            return;
        }

        this.notifyRegisteredListeners(listener -> listener.currentDocumentChanged(model, this.currentDocument));
        model.setFilePath(newPath);
        model.setModified(false);
        setIconAt(this.getSelectedIndex(), this.documentNotModifiedIcon);
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int oldIndex = this.openedDocuments.indexOf(model);
        int newIndex = oldIndex >= this.getNumberOfDocuments() - 1 ? oldIndex - 1 : oldIndex;
        if (!this.openedDocuments.remove(model)) return;
        this.notifyRegisteredListeners(listener -> listener.documentRemoved(model));
        this.removeTabAt(oldIndex);

        this.currentDocument = !this.openedDocuments.isEmpty() ? this.currentDocument = this.openedDocuments.get(newIndex) : null;
        this.notifyRegisteredListeners(listener -> listener.currentDocumentChanged(model, this.currentDocument));
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        this.listeners.add(Objects.requireNonNull(l, "The given listener that is to be added cannot be null!"));
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        this.listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return this.openedDocuments.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return this.openedDocuments.get(index);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return this.openedDocuments.iterator();
    }

    /**
     * Notifies all listeners within the {@code listeners}, registered for receiving events from the current model.
     *
     * @param action action that is performed for every registered listener.
     */
    private void notifyRegisteredListeners(Consumer<MultipleDocumentListener> action) {
        this.listeners.forEach(action);
    }

    /**
     * Locates the icon at the given {@code source} path.
     *
     * @param source relative path of the wanted icon.
     * @return a new {@link ImageIcon} instance.
     */
    private ImageIcon useIcon(String source) {
        try(InputStream is = this.getClass().getResourceAsStream(Objects.requireNonNull(source, "The given icon source path cannot be null!"))) {
            if(is == null) {
                JNotepadPP.informUser(
                        this,
                        String.format(this.flp.getString("errorOpenIS"), source),
                        this.flp.getString("error"),
                        JOptionPane.ERROR_MESSAGE,
                        new String[]{this.flp.getString("ok")});
                return null;
            }
            Image toBeResized = new ImageIcon(is.readAllBytes()).getImage();
            return new ImageIcon(toBeResized.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            JNotepadPP.informUser(this,
                    String.format(this.flp.getString("errorReadingFromIS"), source),
                    this.flp.getString("error"),
                    JOptionPane.ERROR_MESSAGE,
                    new String[]{this.flp.getString("ok")});
            return null;
        }
    }
}
