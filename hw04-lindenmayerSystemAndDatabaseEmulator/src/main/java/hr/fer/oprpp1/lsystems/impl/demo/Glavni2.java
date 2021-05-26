package hr.fer.oprpp1.lsystems.impl.demo;

import hr.fer.oprpp1.lsystems.impl.LSystemBuilderImpl;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;

/**
 * The {@code Glavni2} class is a program for creating a Koch curve through text configuration.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Glavni2 {

    /**
     * Method taken from this homework's PDF dedicated to demonstrating {@link LSystem} capabilities.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        LSystemViewer.showLSystem(createKochCurve2(LSystemBuilderImpl::new));
    }

    /**
     * Method taken from this homework's PDF dedicated to demonstrating the creation of a Koch curve.
     *
     * @param provider {@link LSystemBuilderProvider} instance.
     */
    private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
        String[] data = new String[] {
                "origin 0.05 0.4",
                "angle 0",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0 / 3.0",
                "",
                "command & color 008080",
                "command F draw 1",
                "command + rotate 60",
                "command - rotate -60",
                "",
                "axiom F",
                "",
                "production F F+F-&-F+F"
        };
        return provider.createLSystemBuilder().configureFromText(data).build();
    }
}
