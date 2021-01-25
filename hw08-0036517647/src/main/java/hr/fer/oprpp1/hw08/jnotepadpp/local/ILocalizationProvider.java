package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * The {@code ILocalizationProvider} interface that models a localization provider.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface ILocalizationProvider {
    /**
     * Retrieves the string value related to the given {@code key}
     *
     * @param key the key to the wanted value.
     * @return the value related to the given {@code key}.
     */
    String getString(String key);

    /**
     * Registers the given {@code listener} to the current provider.
     *
     * @param listener the listener that is to be registered to the current provider.
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Unregisters the given {@code listener} from the current provider.
     *
     * @param listener the listener that is to be unregistered from the current provider.
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Retrieves the currently provided language.
     *
     * @return the currently provided language.
     */
    String getCurrentLanguage();
}
