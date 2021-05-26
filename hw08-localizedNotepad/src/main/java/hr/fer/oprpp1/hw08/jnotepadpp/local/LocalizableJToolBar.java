package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * The {@code LocalizableJToolBar} class represents an localizable instance of {@link JToolBar}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LocalizableJToolBar extends JToolBar {
    @java.io.Serial
    private static final long serialVersionUID = 1920450000000000001L;
    /**
     * The key used for the retrieval of the value from the given provider.
     */
    private final String key;
    /**
     * The provider used to retrieve localization information.
     */
    private final ILocalizationProvider prov;

    /**
     * Creates a new {@code LocalizableJToolBar} with the given {@code key} that is retrieved through the {@code lp}.
     *
     * @param key the key used for the retrieval of the value from the given provider.
     * @param lp the provider used to retrieve localization information.
     */
    public LocalizableJToolBar(String key, ILocalizationProvider lp) {
        this.key = key;
        this.prov = lp;
        this.prov.addLocalizationListener(this::setLocalizedText);
        this.setLocalizedText();
    }

    /**
     * Sets the localized text of the current {@link JToolBar} instance.
     */
    private void setLocalizedText() {
        this.setName(this.prov.getString(this.key));
    }

}
