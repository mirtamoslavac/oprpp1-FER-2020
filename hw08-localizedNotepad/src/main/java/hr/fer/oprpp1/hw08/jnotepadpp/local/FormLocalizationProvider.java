package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The {@code FormLocalizationProvider} class implements {@link LocalizationProviderBridge}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
    /**
     * Creates a new {@code FormLocalizationProvider} and implements actions for opening and closing,
     * while respectively connecting and disconnecting the {@code parent} and the current bridge.
     *
     * @param parent the provider to which to connect the current bridge.
     * @param jFrame the frame that seeks localization information and is is being observed by the bridge.
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame jFrame) {
        super(parent);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                disconnect();
            }
        });
    }
}
