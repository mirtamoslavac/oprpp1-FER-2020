package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * The {@code LocalizableAction} class represents an localizable implementation of the {@link AbstractAction}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public abstract class LocalizableAction extends AbstractAction {
    @java.io.Serial
    private static final long serialVersionUID = 7413215684941320360L;
    /**
     * Suffix for locating localization string in the respective resource.
     */
    private static final String DESCRIPTION = "Description";
    /**
     * The key used for the retrieval of the value from the given provider.
     */
    private final String key;
    /**
     * The provider used to retrieve localization information.
     */
    private final ILocalizationProvider prov;
    /**
     * Creates a new {@code LocalizableAction} with the given {@code key} that is retrieved through the {@code lp}.
     *
     * @param key the key used for the retrieval of the value from the given provider.
     * @param lp the provider used to retrieve localization information.
     */
    public LocalizableAction(String key, ILocalizationProvider lp) {
        this.key = key;
        this.prov = lp;
        this.prov.addLocalizationListener(this::putValues);
        this.putValues();
    }

    /**
     * Sets the localized values with associated action.
     */
    private void putValues() {
        this.putValue(Action.NAME, this.prov.getString(this.key));
        if (this.key.length() != 2) this.putValue(Action.SHORT_DESCRIPTION, this.prov.getString(this.key + DESCRIPTION));
    }
}
