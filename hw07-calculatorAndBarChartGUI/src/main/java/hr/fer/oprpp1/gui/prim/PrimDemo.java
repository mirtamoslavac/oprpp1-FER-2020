package hr.fer.oprpp1.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code PrimDemo} class is a swing application displaying two equal lists of dynamically generated primes.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class PrimDemo extends JFrame {
    @java.io.Serial
    private static final long serialVersionUID = 1346798522589764314L;
    /**
     * Creates a new {@code PrimDemo} instance.
     */
    public PrimDemo() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lists of prime numbers");
        this.initGUI();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Initializes the application GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        PrimListModel primeModel = new PrimListModel();

        JPanel p = new JPanel(new GridLayout(1, 0));
        p.add(new JScrollPane(new JList<>(primeModel)));
        p.add(new JScrollPane(new JList<>(primeModel)));
        cp.add(p, BorderLayout.CENTER);

        JButton b = new JButton("Next!");
        cp.add(b, BorderLayout.PAGE_END);
        b.addActionListener(event -> primeModel.next());
    }

    /**
     * Starts the prime application.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new PrimDemo().setVisible(true));
    }
}
