package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The {@code LocalizationProvider} class is an implementation of the {@code AbstractLocalizationProvider}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
    /**
     * The language in which the string values will be returned.
     */
    private String language;
    /**
     * The {@code ResourceBundle} instance that contains all localized information.
     */
    private ResourceBundle bundle;
    /**
     * The singleton instance of the provider.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * Creates a singleton {@code LocalizationProvider} instance and sets the default language as {@code en}.
     */
    private LocalizationProvider() {
        this.language = "en";
        this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.translations", Locale.forLanguageTag(this.language));
    }

    /**
     * Returns the singleton instance of the provider.
     *
     * @return the singleton instance of the provider.
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets the language of the provider to the given {@code language}.
     *
     * @param language new language that is to be provided.
     */
    public void setLanguage(String language) {
        this.language = language;
        this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.translations", Locale.forLanguageTag(language));
        this.fire();
    }

    @Override
    public String getString(String key) {
        return this.bundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return this.language;
    }
}
