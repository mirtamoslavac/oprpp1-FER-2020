package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AbstractLocalizationProvider} abstract class represents an implementation of {@link ILocalizationListener}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    /**
     * List of registered listeners to the provider.
     */
    private final List<ILocalizationListener> listeners;

    /**
     * Creates a new {@code AbstractLocalizationProvider} instance.
     */
    public AbstractLocalizationProvider() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners about the change of localization.
     */
    public void fire() {
        this.listeners.forEach(ILocalizationListener::localizationChanged);
    }
}
