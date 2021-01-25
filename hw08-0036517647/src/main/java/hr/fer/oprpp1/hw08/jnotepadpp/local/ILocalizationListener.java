package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * The {@code ILocalizationListener} interface represents a model of the listener that registers to be notified of a change in localization.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
@FunctionalInterface
public interface ILocalizationListener {
    /**
     * Notifies the listener about the change in localization.
     */
    void localizationChanged();
}
