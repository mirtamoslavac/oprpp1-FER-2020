package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * The {@code LocalizableJMenu} class represents an localizable instance of {@link JMenu}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LocalizableJMenu extends JMenu {
    @java.io.Serial
    private static final long serialVersionUID = 1710500561900341809L;
    /**
     * The key used for the retrieval of the value from the given provider.
     */
    private final String key;
    /**
     * The provider used to retrieve localization information.
     */
    private final ILocalizationProvider prov;

    /**
     * Creates a new {@code LocalizableJMenu} with the given {@code key} that is retrieved through the {@code lp}.
     *
     * @param key the key used for the retrieval of the value from the given provider.
     * @param lp the provider used to retrieve localization information.
     */
    public LocalizableJMenu(String key, ILocalizationProvider lp) {
        this.key = key;
        this.prov = lp;
        this.prov.addLocalizationListener(this::setLocalizedText);
        this.setLocalizedText();
    }

    /**
     * Sets the localized text of the current {@link JMenu} instance.
     */
    private void setLocalizedText() {
        this.setText(this.prov.getString(this.key));
    }

}
