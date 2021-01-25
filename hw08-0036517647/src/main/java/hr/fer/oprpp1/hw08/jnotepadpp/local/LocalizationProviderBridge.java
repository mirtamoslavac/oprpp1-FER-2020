package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * The {@code LocalizationProviderBridge} class is an implementation of the {@code AbstractLocalizationProvider}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    /**
     * The state of connection of the current bridge.
     */
    private boolean connected;
    /**
     * The listener used for the connection.
     */
    private final ILocalizationListener listener;
    /**
     * The provider to which the bridge connects to.
     */
    private final ILocalizationProvider parent;
    /**
     * Caches the previous language if the bridge is reconnected to the source
     * which had the language changed while the bridge was disconnected.
     */
    private String cachedLanguage;

    /**
     * Creates a new {@code LocalizationProviderBridge} with the given.
     *
     * @param parent the provider to which the bridge connects to.
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = parent;
        this.listener = this::fire;
        this.cachedLanguage = null;
    }

    /**
     * Creates a connection between the current bridge and the {@code parent} through a listener.
     */
    public void connect() {
        if (this.connected) return;
        this.connected = true;
        this.parent.addLocalizationListener(this.listener);
        if (this.cachedLanguage != null && !this.cachedLanguage.equals(this.parent.getCurrentLanguage())) {
            fire();
            this.cachedLanguage = this.parent.getCurrentLanguage();
        }
    }

    /**
     * Disconnects the current bridge fom the {@code parent}, if previously connected.
     */
    public void disconnect() {
        if (!this.connected) return;
        this.connected = false;
        this.parent.removeLocalizationListener(this.listener);
        this.cachedLanguage = this.parent.getCurrentLanguage();
    }

    @Override
    public String getString(String key) {
        return this.parent.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return this.parent.getCurrentLanguage();
    }
}
