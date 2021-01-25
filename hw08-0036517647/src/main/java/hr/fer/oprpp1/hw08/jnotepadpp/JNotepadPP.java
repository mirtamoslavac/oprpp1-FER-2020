package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.document.implementation.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

/**
 * The {@code JNotepadPP} class is a simple Swing text editor, allowing the user to work with multiple documents at the same time.
 *
 * @author mirtamoslavac
 * @version 1.2
 */
public class JNotepadPP extends JFrame {
    @java.io.Serial
    private static final long serialVersionUID = 1593578522584657913L;
    /**
     * Title of the text editor application.
     */
    public static final String APPLICATION_TITLE = "JNotepad++";
    /**
     * The {@link DefaultMultipleDocumentModel} instance that is used within the application to store opened documents that are to be edited.
     */
    private DefaultMultipleDocumentModel multipleDocumentModel;
    /**
     * The localization provider for the application.
     */
    private FormLocalizationProvider flp;
    /**
     * The localized option of a informational pop-up message within the application.
     */
    private String informUserOption;
    /**
     * Labels that contain information within the status bar.
     */
    private final JLabel lengthLabel = new JLabel(),
            documentInfoLabel = new JLabel(),
            dateAndTimeLabel = new JLabel();
    /**
     * The clock within the status bar.
     */
    private Timer clock;
    /**
     * Localizable actions that can be performed by the user within the application.
     */
    private LocalizableAction createBlankDocumentAction, openDocumentAction, saveDocumentAction, saveDocumentAsAction,
            closeCurrentDocumentAction, copyTextAction, cutTextAction, pasteTextAction, getDocumentStatisticsAction, exitAction,
            toLowercaseAction, toUppercaseAction, invertCaseAction, sortAscendingAction, sortDescendingAction, uniqueAction,
            changeLanguageToEnglishAction, changeLanguageToCroatianAction, changeLanguageToGermanAction;

    /**
     * Creates a new {@code JNotepadPP} instance and initializes it.
     */
    public JNotepadPP() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(0, 0);
        setSize(600, 600);
        setTitle(APPLICATION_TITLE);

        initGUI();
        setUpWindowListener();
        setUpMultipleDocumentListener();
    }

    /**
     * Adds a window listener to the application in order to properly close it.
     */
    private void setUpWindowListener() {
        WindowListener wl = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        };

        this.addWindowListener((wl));
    }

    /**
     * Sets up a multiple document listener implementation with respective single document listener and caret listener implementations.
     */
    private void setUpMultipleDocumentListener() {
        //setup singleDocumentListener for MDL
        SingleDocumentListener sdl = new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                saveDocumentAction.setEnabled(model.isModified());
                //uncomment if wanting to have save only enabled when editing already existing files
                //saveDocumentAction.setEnabled(model.getFilePath() != null && model.isModified());
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                multipleDocumentModel.setTitleAt(multipleDocumentModel.getSelectedIndex(),
                        model.getFilePath() == null ? flp.getString("unnamed") : model.getFilePath().getFileName().toString());
                multipleDocumentModel.setToolTipTextAt(multipleDocumentModel.getSelectedIndex(),
                        model.getFilePath() == null ? flp.getString("unnamed") : model.getFilePath().toString());
            }
        };

        MultipleDocumentListener mdl = new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel == null && currentModel == null) throw new IllegalArgumentException("The given previous and current models cannot both be null!");
                if (previousModel != null) {
                    previousModel.removeSingleDocumentListener(sdl);
                }
                if (currentModel != null) {
                    currentModel.addSingleDocumentListener(sdl);
                }

                if (currentModel == null) {
                    setTitle(APPLICATION_TITLE);
                    setDocumentActionEnablement(false);
                    setDocumentModificationActionEnablement(false);
                    lengthLabel.setText("");
                    documentInfoLabel.setText("");
                } else {
                    Caret caret = currentModel.getTextComponent().getCaret();
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    setDocumentModificationActionEnablement(dot != mark);
                    setDocumentActionEnablement(true);
                    saveDocumentAction.setEnabled(currentModel.isModified());
                    setTitle((currentModel.getFilePath() == null ?
                            flp.getString("unnamed") :
                            currentModel.getFilePath().getFileName().toString()) + " - " + APPLICATION_TITLE);

                    setLength(currentModel);
                    setDocumentInfo(currentModel);
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                model.getTextComponent().getCaret().addChangeListener(e -> {
                    Caret caret = model.getTextComponent().getCaret();
                    setDocumentModificationActionEnablement(caret.getDot() != caret.getMark());

                    setLength(model);
                    setDocumentInfo(model);
                });
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
            }
        };

        this.multipleDocumentModel.addMultipleDocumentListener(mdl);
    }

    /**
     * Sets the length label for the given {@code currentDocument} within the status bar.
     *
     * @param currentDocument the document whose length is to be displayed.
     */
    private void setLength(SingleDocumentModel currentDocument) {
        int length = currentDocument.getTextComponent().getDocument().getLength();
        this.lengthLabel.setText(String.format(flp.getString("length"), length != 0 ? String.valueOf(length) : "-"));
    }

    /**
     * Sets the document information label for the given {@code currentDocument} within the status bar.
     *
     * @param currentDocument the document whose length is to be displayed.
     */
    private void setDocumentInfo(SingleDocumentModel currentDocument) {
        JTextArea editor = currentDocument.getTextComponent();
        int caretPosition = editor.getCaretPosition();
        Element root = editor.getDocument().getDefaultRootElement();
        int lineIndex = root.getElementIndex(caretPosition);

        int lineNumber = lineIndex + 1;
        int columns = caretPosition - root.getElement(lineIndex).getStartOffset() + 1;
        int selectionLength = editor.getSelectionEnd() - editor.getSelectionStart();

        this.documentInfoLabel.setText(String.format(flp.getString("documentInfo"),
                lineNumber != 0 ? String.valueOf(lineNumber) : "-",
                columns != 0 ? String.valueOf(columns) : "-",
                selectionLength != 0 ? String.valueOf(selectionLength) : "-"));
    }

    /**
     * Initializes the GUI of the application.
     */
    private void initGUI() {
        this.flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
        this.multipleDocumentModel = new DefaultMultipleDocumentModel(this.flp);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.multipleDocumentModel, BorderLayout.CENTER);
        this.informUserOption = this.flp.getString("ok");

        createActions();
        setUpActions();
        createMenus();
        createDockableToolbar();
        createStatusBar();
    }

    /**
     * Initializes all actions present within the application.
     */
    private void createActions() {
        this.createBlankDocumentAction = new LocalizableAction("createBlankDocumentAction", this.flp) {
            private static final long serialVersionUID = 3223560124922222221L;

            @Override
            public void actionPerformed(ActionEvent e) {
                multipleDocumentModel.createNewDocument();
            }
        };

        this.openDocumentAction = new LocalizableAction("openDocumentAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 3454893234785100860L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(flp.getString("openDocument"));
                if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) return;

                File fileName = fc.getSelectedFile();
                Path filePath = fileName.toPath();
                if (!Files.exists(filePath)) {
                    informUser(JNotepadPP.this,
                            String.format(flp.getString("fileNotExist"), fileName.getAbsolutePath()),
                            flp.getString("error"),
                            JOptionPane.ERROR_MESSAGE,
                            new String[]{informUserOption});
                } else if (!Files.isWritable(filePath)) {
                    informUser(JNotepadPP.this,
                            String.format(flp.getString("fileNotModifiable"), fileName.getAbsolutePath()),
                            flp.getString("error"),
                            JOptionPane.ERROR_MESSAGE,
                            new String[]{informUserOption});
                }

                multipleDocumentModel.loadDocument(filePath);
            }
        };

        this.saveDocumentAction = new LocalizableAction("saveDocumentAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 1582710002136222738L;

            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocument(false);
            }
        };

        this.saveDocumentAsAction = new LocalizableAction("saveDocumentAsAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 7419641255541234557L;

            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocumentAs(false);
            }
        };

        this.closeCurrentDocumentAction = new LocalizableAction("closeCurrentDocumentAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 1111000001010010115L;

            @Override
            public void actionPerformed(ActionEvent e) {
                closeDocument(multipleDocumentModel.getCurrentDocument());
            }
        };

        this.copyTextAction = new LocalizableAction("copyTextAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 2485332222635433692L;
            private final AbstractAction dekCopy = new DefaultEditorKit.CopyAction();

            @Override
            public void actionPerformed(ActionEvent e) {
                this.dekCopy.actionPerformed(e);
            }
        };

        this.cutTextAction = new LocalizableAction("cutTextAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 1846513212332154678L;
            private final AbstractAction dekCut = new DefaultEditorKit.CutAction();

            @Override
            public void actionPerformed(ActionEvent e) {
                this.dekCut.actionPerformed(e);
            }
        };

        this.pasteTextAction = new LocalizableAction("pasteTextAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 6357114448000431896L;
            private final AbstractAction dekPaste = new DefaultEditorKit.PasteAction();

            @Override
            public void actionPerformed(ActionEvent e) {
                this.dekPaste.actionPerformed(e);
            }
        };

        this.getDocumentStatisticsAction = new LocalizableAction("getDocumentStatisticsAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 1375860347923199293L;

            @Override
            public void actionPerformed(ActionEvent e) {
                SingleDocumentModel currentDocument = multipleDocumentModel.getCurrentDocument();

                String documentText = currentDocument.getTextComponent().getText();
                int characterCount = documentText.length();
                int nonBlankCharacterCount = documentText.replaceAll("\\s+", "").length();
                int lineCount = currentDocument.getTextComponent().getLineCount();
                String indentation = "\t ".repeat(7);
                informUser(JNotepadPP.this,
                        String.format(flp.getString("statisticalInfo"), indentation, characterCount, indentation, nonBlankCharacterCount, indentation, lineCount),
                        flp.getString("statisticalInfoTitle"),
                        JOptionPane.INFORMATION_MESSAGE, new String[]{informUserOption});
            }
        };

        this.exitAction = new LocalizableAction("exitAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 3753333333690111110L;

            @Override
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        };

        this.toUppercaseAction = new LocalizableAction("toUppercaseAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 9127863796666412345L;

            @Override
            public void actionPerformed(ActionEvent e) {
                caseAction(JNotepadPP.CaseActionType.UPPERCASE);
            }
        };

        this.toLowercaseAction = new LocalizableAction("toLowercaseAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 2431685794281112480L;

            @Override
            public void actionPerformed(ActionEvent e) {
                caseAction(JNotepadPP.CaseActionType.LOWERCASE);
            }
        };

        this.invertCaseAction = new LocalizableAction("invertCaseAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 8254873112345678538L;

            @Override
            public void actionPerformed(ActionEvent e) {
                caseAction(JNotepadPP.CaseActionType.INVERT_CASE);
            }
        };

        this.uniqueAction = new LocalizableAction("uniqueAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 1357908642148963000L;

            @Override
            public void actionPerformed(ActionEvent e) {
                performSelectionModification(text -> String.join("\n", new LinkedHashSet<>(Arrays.asList(text.split("\\r?\\n")))) + "\n", true);
            }
        };

        this.sortAscendingAction = new LocalizableAction("sortAscendingAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 4635891255555557200L;

            @Override
            public void actionPerformed(ActionEvent e) {
                sortAction(JNotepadPP.SortActionType.ASCENDING);
            }
        };

        this.sortDescendingAction = new LocalizableAction("sortDescendingAction", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 986451321657561320L;

            @Override
            public void actionPerformed(ActionEvent e) {
                sortAction(JNotepadPP.SortActionType.DESCENDING);
            }
        };

        this.changeLanguageToEnglishAction = new LocalizableAction("en", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 8923135465432131554L;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
                adaptForNewLanguage();
            }
        };

        this.changeLanguageToCroatianAction = new LocalizableAction("hr", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 986512316846513211L;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
                adaptForNewLanguage();
            }
        };

        this.changeLanguageToGermanAction = new LocalizableAction("de", this.flp) {
            @java.io.Serial
            private static final long serialVersionUID = 3333375444812111111L;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("de");
                adaptForNewLanguage();
            }
        };
    }

    /**
     * Sets up initial states for all actions and their enablement states after starting the application.
     */
    private void setUpActions() {
        initiateAction(this.createBlankDocumentAction, "control N", KeyEvent.VK_N);
        initiateAction(this.openDocumentAction, "control O", KeyEvent.VK_O);
        initiateAction(this.saveDocumentAction, "control S", KeyEvent.VK_S);
        initiateAction(this.saveDocumentAsAction, "control shift S", KeyEvent.VK_A);
        initiateAction(this.closeCurrentDocumentAction, "control W", KeyEvent.VK_W);
        initiateAction(this.copyTextAction, "control C", KeyEvent.VK_C);
        initiateAction(this.cutTextAction, "control X", KeyEvent.VK_X);
        initiateAction(this.pasteTextAction, "control V", KeyEvent.VK_V);
        initiateAction(this.getDocumentStatisticsAction, "control T", KeyEvent.VK_T);
        initiateAction(this.exitAction, "control E", KeyEvent.VK_E);
        initiateAction(this.toLowercaseAction, "control L", KeyEvent.VK_L);
        initiateAction(this.toUppercaseAction, "control U", KeyEvent.VK_U);
        initiateAction(this.invertCaseAction, "control I", KeyEvent.VK_I);
        initiateAction(this.sortAscendingAction, "control shift A", KeyEvent.VK_A);
        initiateAction(this.sortDescendingAction, "control shift D", KeyEvent.VK_D);
        initiateAction(this.uniqueAction, "control U", KeyEvent.VK_U);
        initiateAction(this.changeLanguageToEnglishAction, "control shift E", KeyEvent.VK_E);
        initiateAction(this.changeLanguageToCroatianAction, "control shift H", KeyEvent.VK_H);
        initiateAction(this.changeLanguageToGermanAction, "control shift G", KeyEvent.VK_G);

        this.saveDocumentAction.setEnabled(false);
        setDocumentActionEnablement(false);
        setDocumentModificationActionEnablement(false);
        setChangeLanguageActionEnablement(this.flp.getCurrentLanguage());
    }

    /**
     * Sets the given values of the given {@code action}.
     *
     * @param action         the action that is to be assigned with new values.
     * @param acceleratorKey the keyboard shortcut for performing the application.
     * @param mnemonicKey    mnemonic key for determining the key stroke assigned to the given action.
     */
    private void initiateAction(Action action, String acceleratorKey, int mnemonicKey) {
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(acceleratorKey));
        action.putValue(Action.MNEMONIC_KEY, mnemonicKey);
    }

    /**
     * Sets the enablement status of actions related to document management.
     *
     * @param isEnabled {@code true} if the actions are to be enabled, {@code false} if otherwise.
     */
    private void setDocumentActionEnablement(boolean isEnabled) {
        this.saveDocumentAction.setEnabled(isEnabled);
        //uncomment if wanting to have save only enabled when editing already existing files
        /*this.saveDocumentAction.setEnabled(this.multipleDocumentModel.getCurrentDocument() != null &&
                this.multipleDocumentModel.getCurrentDocument().getFilePath() != null &&
                this.multipleDocumentModel.getCurrentDocument().isModified());*/
        this.saveDocumentAsAction.setEnabled(isEnabled);
        this.closeCurrentDocumentAction.setEnabled(isEnabled);
        this.getDocumentStatisticsAction.setEnabled(isEnabled);
        this.closeCurrentDocumentAction.setEnabled(isEnabled);
        this.pasteTextAction.setEnabled(isEnabled && Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor));
    }

    /**
     * Sets the enablement status of actions related to document modification.
     *
     * @param isEnabled {@code true} if the actions are to be enabled, {@code false} if otherwise.
     */
    private void setDocumentModificationActionEnablement(boolean isEnabled) {
        this.copyTextAction.setEnabled(isEnabled);
        this.cutTextAction.setEnabled(isEnabled);
        this.toLowercaseAction.setEnabled(isEnabled);
        this.toUppercaseAction.setEnabled(isEnabled);
        this.invertCaseAction.setEnabled(isEnabled);
        this.sortAscendingAction.setEnabled(isEnabled);
        this.sortDescendingAction.setEnabled(isEnabled);
        this.uniqueAction.setEnabled(isEnabled);
    }

    /**
     * Sets the enablement status of the active language and other available languages.
     *
     * @param currentLanguage the language whose respective action is to be disabled, while others will be enabled.
     */
    private void setChangeLanguageActionEnablement(String currentLanguage) {
        this.changeLanguageToEnglishAction.setEnabled(!currentLanguage.equals("en"));
        this.changeLanguageToCroatianAction.setEnabled(!currentLanguage.equals("hr"));
        this.changeLanguageToGermanAction.setEnabled(!currentLanguage.equals("de"));
    }

    /**
     * Creates the menu bar within the application.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LocalizableJMenu("file", flp);
        menuBar.add(fileMenu);
        fileMenu.add(new JMenuItem(this.createBlankDocumentAction));
        fileMenu.add(new JMenuItem(this.openDocumentAction));
        fileMenu.add(new JMenuItem(this.saveDocumentAction));
        fileMenu.add(new JMenuItem(this.saveDocumentAsAction));
        fileMenu.add(new JMenuItem(this.getDocumentStatisticsAction));
        fileMenu.add(new JMenuItem(this.closeCurrentDocumentAction));
        fileMenu.add(new JMenuItem(this.exitAction));

        JMenu editMenu = new LocalizableJMenu("edit", flp);
        menuBar.add(editMenu);
        editMenu.add(new JMenuItem(this.copyTextAction));
        editMenu.add(new JMenuItem(this.cutTextAction));
        editMenu.add(new JMenuItem(this.pasteTextAction));

        JMenu toolsMenu = new LocalizableJMenu("tools", flp);
        menuBar.add(toolsMenu);
        JMenu caseSubmenu = new LocalizableJMenu("changeCase", flp);
        toolsMenu.add(caseSubmenu);
        caseSubmenu.add(new JMenuItem(this.toUppercaseAction));
        caseSubmenu.add(new JMenuItem(this.toLowercaseAction));
        caseSubmenu.add(new JMenuItem(this.invertCaseAction));

        JMenu sortSubmenu = new LocalizableJMenu("sort", flp);
        toolsMenu.add(sortSubmenu);
        sortSubmenu.add(new JMenuItem(this.sortAscendingAction));
        sortSubmenu.add(new JMenuItem(this.sortDescendingAction));

        toolsMenu.add(new JMenuItem(this.uniqueAction));

        JMenu languagesMenu = new LocalizableJMenu("languages", flp);
        menuBar.add(languagesMenu);
        languagesMenu.add(new JMenuItem(this.changeLanguageToEnglishAction));
        languagesMenu.add(new JMenuItem(this.changeLanguageToCroatianAction));
        languagesMenu.add(new JMenuItem(this.changeLanguageToGermanAction));

        this.setJMenuBar(menuBar);
    }

    /**
     * Creates the dockable toolbar within the application.
     */
    private void createDockableToolbar() {
        JToolBar toolbar = new LocalizableJToolBar("toolbar", flp);
        toolbar.setFloatable(true);

        toolbar.add(new JButton(this.createBlankDocumentAction));
        toolbar.add(new JButton(this.openDocumentAction));
        toolbar.add(new JButton(this.saveDocumentAction));
        toolbar.add(new JButton(this.saveDocumentAsAction));
        toolbar.add(new JButton(this.getDocumentStatisticsAction));
        toolbar.add(new JButton(this.closeCurrentDocumentAction));
        toolbar.addSeparator();
        toolbar.add(new JButton(this.copyTextAction));
        toolbar.add(new JButton(this.cutTextAction));
        toolbar.add(new JButton(this.pasteTextAction));
        toolbar.addSeparator();
        toolbar.add(new JButton(this.exitAction));

        this.getContentPane().add(toolbar, BorderLayout.PAGE_START);
    }

    /**
     * Creates the status bar within the application.
     */
    private void createStatusBar() {
        JToolBar statusBar = new LocalizableJToolBar("statusBar", this.flp);
        statusBar.setFloatable(false);
        statusBar.setLayout(new GridLayout(1, 2));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 2));
        infoPanel.add(this.lengthLabel);
        infoPanel.add(this.documentInfoLabel);
        statusBar.add(infoPanel);

        startClock();
        this.dateAndTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusBar.add(this.dateAndTimeLabel);

        this.getContentPane().add(statusBar, BorderLayout.PAGE_END);
    }

    /**
     * Creates and starts the clock within the status bar within the application.
     */
    private void startClock() {
        DateTimeFormatter dateAndTimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        this.clock = new Timer(100, e -> this.dateAndTimeLabel.setText(dateAndTimeFormat.format(LocalDateTime.now())));
        this.clock.start();
    }

    /**
     * Saves the currently open document.
     *
     * @param close {@code true} the saving occurs while closing the document, {@code false} otherwise.
     */
    private void saveDocument(boolean close) {
        SingleDocumentModel currentDocument = this.multipleDocumentModel.getCurrentDocument();

        if (currentDocument.getFilePath() == null) {
            saveDocumentAs(close);
//            try {
//                currentDocument.setFilePath(getPathForSaveAs());
//            } catch (NullPointerException e) {
//                if (close) throw new NullPointerException();
//                else return;
//            }
//            this.multipleDocumentModel.saveDocument(currentDocument, currentDocument.getFilePath());
        } else this.multipleDocumentModel.saveDocument(currentDocument, null);
    }

    /**
     * Saves the currently open document to a new path.
     *
     * @param close {@code true} the saving occurs while closing the document, {@code false} otherwise.
     */
    private void saveDocumentAs(boolean close) {
        SingleDocumentModel currentDocument = this.multipleDocumentModel.getCurrentDocument();
        Path newPath;
        try {
            newPath = getPathForSaveAs();
            if (newPath == null) throw new NullPointerException();
        } catch (NullPointerException e1) {
            if (close) throw new NullPointerException();
            else return;
        }
        if (new File(newPath.toAbsolutePath().normalize().toString()).exists()) {
            String[] options = new String[]{flp.getString("yes"), this.flp.getString("no"), this.flp.getString("cancel")};
            int res = JOptionPane.showOptionDialog(JNotepadPP.this,
                    String.format(flp.getString("alreadyExists"), newPath.getFileName()),
                    this.flp.getString("warning"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            switch (res) {
                case JOptionPane.CLOSED_OPTION:
                case 2:
                case 1:
                    return;
                case 0:
                    try {
                        this.multipleDocumentModel.saveDocument(currentDocument, newPath);
                        currentDocument.setFilePath(newPath);
                        setTitle(currentDocument.getFilePath().getFileName().toString() + " - " + APPLICATION_TITLE);
                    } catch (NullPointerException e1) {
                        return;
                    }
                    return;
            }
        }

        this.multipleDocumentModel.saveDocument(currentDocument, currentDocument.getFilePath());
        currentDocument.setFilePath(newPath);
        setTitle(currentDocument.getFilePath().getFileName().toString() + " - " + APPLICATION_TITLE);
    }

    /**
     * Retrieves a new path to which the currently opened document is to be saved.
     *
     * @return the chosen path.
     */
    private Path getPathForSaveAs() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(this.flp.getString("saveDocument"));
        if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
            informUser(JNotepadPP.this,
                    this.flp.getString("nothingSaved"),
                    this.flp.getString("warning"),
                    JOptionPane.ERROR_MESSAGE,
                    new String[]{this.informUserOption});
            return null;
        }
        return jfc.getSelectedFile().toPath();
    }

    /**
     * Closes the given {@code document}
     *
     * @param document {@link SingleDocumentModel} instance opened in the application that is to be closed.
     * @return {@code true} if the document was closed successfully, {@code false} otherwise.
     */
    private boolean closeDocument(SingleDocumentModel document) {
        SingleDocumentModel currentDocument = this.multipleDocumentModel.getCurrentDocument();

        if (currentDocument.isModified()) {
            String[] options = new String[]{this.flp.getString("yes"), this.flp.getString("no"), this.flp.getString("cancel")};
            int res = JOptionPane.showOptionDialog(JNotepadPP.this,
                    document.getFilePath() == null ?
                            this.flp.getString("newDocumentUnsavedClose") :
                            String.format(flp.getString("fileUnsavedClose"), document.getFilePath().getFileName()),
                    this.flp.getString("unsavedChanges"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            switch (res) {
                case JOptionPane.CLOSED_OPTION:
                case 2:
                    return false;
                case 0:
                    try {
                        saveDocument(true);
                    } catch (NullPointerException e) {
                        return false;
                    }
                    break;
                case 1:
                    break;
            }
        }
        this.multipleDocumentModel.closeDocument(currentDocument);
        return true;
    }

    /**
     * Exits the entire application while checking for unsaved documents.
     */
    private void exitApplication() {
        while (this.multipleDocumentModel.getCurrentDocument() != null) {
            this.multipleDocumentModel.setSelectedIndex(0);
            if (!closeDocument(this.multipleDocumentModel.getDocument(0))) return;
        }

        this.clock.stop();
        this.dispose();
    }

    /**
     * Implements a generic action for text case modification.
     *
     * @param caseActionType type of text case modification that is to be executed.
     */
    private void caseAction(JNotepadPP.CaseActionType caseActionType) {
        performSelectionModification(text -> {
            char[] characters = text.toCharArray();
            for (int i = 0, length = characters.length; i < length; i++) {
                char c = characters[i];
                switch (caseActionType) {
                    case LOWERCASE:
                        if (Character.isUpperCase(c)) characters[i] = Character.toLowerCase(c);
                        break;
                    case UPPERCASE:
                        if (Character.isLowerCase(c)) characters[i] = Character.toUpperCase(c);
                        break;
                    default: {
                        if (Character.isLowerCase(c)) characters[i] = Character.toUpperCase(c);
                        else if (Character.isUpperCase(c)) characters[i] = Character.toLowerCase(c);
                        break;
                    }
                }
            }
            return new String(characters);
        }, false);
    }

    /**
     * The {@code CaseActionType} enum states possible text case modifications within the application.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private enum CaseActionType {
        /**
         * Selected when transforming the given text to lowercase.
         */
        LOWERCASE,
        /**
         * Selected when transforming the given text to uppercase.
         */
        UPPERCASE,
        /**
         * Selected when toggling the cases within the given text.
         */
        INVERT_CASE
    }

    /**
     * Implements a generic action for row sorting.
     *
     * @param sortActionType type of sorting that is to be executed.
     */
    private void sortAction(JNotepadPP.SortActionType sortActionType) {
        Collator collator = Collator.getInstance(new Locale(this.flp.getCurrentLanguage()));

        performSelectionModification(text -> {
            String[] lines = text.split("\\r?\\n");

            Arrays.sort(lines, sortActionType == SortActionType.ASCENDING ? collator : collator.reversed());

            return String.join("\n", lines) + "\n";
        }, true);
    }

    /**
     * The {@code SortActionType} enum states possible sorting types within the application.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private enum SortActionType {
        /**
         * Selected when wanting to sort the selected text in ascending order.
         */
        ASCENDING,
        /**
         * Selected when wanting to sort the selected text in descending order.
         */
        DESCENDING,
    }

    /**
     * Contains a generic implementation of the selection modification, while the specific modification is given through {@code modification}.
     *
     * @param modification specific act of modification.
     * @param entireLine   determines whether the entire lines of the selected text should be taken into consideration.
     */
    private void performSelectionModification(Function<String, String> modification, boolean entireLine) {
        SingleDocumentModel currentDocument = this.multipleDocumentModel.getCurrentDocument();
        JTextComponent editor = currentDocument.getTextComponent();
        PlainDocument doc = (PlainDocument) editor.getDocument();
        Element root = doc.getDefaultRootElement();

        int fromPos = editor.getSelectionStart(), toPos = editor.getSelectionEnd();
        if (entireLine) {
            fromPos = root.getElement(root.getElementIndex(fromPos)).getStartOffset();
            toPos = Math.min(root.getElement(root.getElementIndex(toPos)).getEndOffset(), doc.getLength());
        }

        try {
            String text = doc.getText(fromPos, toPos - fromPos);
            text = modification.apply(text);
            doc.remove(fromPos, toPos - fromPos);
            doc.insertString(fromPos, text, null);
            this.multipleDocumentModel.getCurrentDocument().getTextComponent().setDocument(doc);
        } catch (BadLocationException ex) {
            informUser(JNotepadPP.this,
                    flp.getString("invalidSelectionModification"),
                    flp.getString("error"),
                    JOptionPane.ERROR_MESSAGE,
                    new String[]{informUserOption});
        }
    }

    /**
     * Makes appropriate editor changes in order to adapt the editor to the newly chosen editor language.
     */
    private void adaptForNewLanguage() {
        setChangeLanguageActionEnablement(this.flp.getCurrentLanguage());
        this.informUserOption = this.flp.getString("ok");

        if (this.multipleDocumentModel.getCurrentDocument() != null) {
            setLocalizedStringsForWindow();
            setLength(this.multipleDocumentModel.getCurrentDocument());
            setDocumentInfo(this.multipleDocumentModel.getCurrentDocument());
        }
    }

    /**
     * Refreshes certain localized application elements to be in accordance with the newly chosen language.
     */
    private void setLocalizedStringsForWindow() {
        for (int i = 0; i < this.multipleDocumentModel.getNumberOfDocuments(); i++) {
            Path filePath = this.multipleDocumentModel.getDocument(i).getFilePath();
            this.multipleDocumentModel.setTitleAt(i,
                    filePath == null ? this.flp.getString("unnamed") : filePath.getFileName().toString());
            this.multipleDocumentModel.setToolTipTextAt(i,
                    filePath == null ? this.flp.getString("unnamed") : filePath.getFileName().toString());
        }

        SingleDocumentModel currentDocument = this.multipleDocumentModel.getCurrentDocument();
        setTitle((currentDocument.getFilePath() == null ?
                this.flp.getString("unnamed") :
                currentDocument.getFilePath().getFileName().toString()) + " - " + APPLICATION_TITLE);
    }

    /**
     * Informs the user about the done action through a pop-up message, an instance of {@link JOptionPane}.
     *
     * @param parent         the frame in which to display the pop-up.
     * @param message        the message of the pop-up.
     * @param title          the title of the pop-up.
     * @param messageType    the type of the pop-up.
     * @param messageOptions the localized option within the pop-up.
     */
    public static void informUser(Component parent, String message, String title, int messageType, String[] messageOptions) {
        int res = JOptionPane.showOptionDialog(parent, message, title, JOptionPane.OK_OPTION, messageType, null, messageOptions, messageOptions[0]);
        switch (res) {
            case JOptionPane.CLOSED_OPTION:
            case 0:
                break;
        }
    }

    /**
     * Starts the text editor application.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }
}
