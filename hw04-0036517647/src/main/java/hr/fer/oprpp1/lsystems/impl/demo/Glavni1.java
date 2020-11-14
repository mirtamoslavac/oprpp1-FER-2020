package hr.fer.oprpp1.lsystems.impl.demo;

import hr.fer.oprpp1.lsystems.impl.LSystemBuilderImpl;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;

import java.awt.*;

/**
 * The {@code Glavni1} class is a program for creating a Koch curve through a {@link hr.fer.zemris.lsystems.LSystemBuilder} instance.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Glavni1 {

    /**
     * Method taken from this homework's PDF dedicated to demonstrating {@link LSystem} capabilities.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
    }

    /**
     * Method taken from this homework's PDF dedicated to demonstrating the creation of a Koch curve.
     *
     * @param provider {@link LSystemBuilderProvider} instance.
     */
    private static LSystem createKochCurve(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 60")
                .registerCommand('-', "rotate -60")
                .setOrigin(0.05, 0.4)
                .setAngle(0)
                .setUnitLength(0.9)
                .setUnitLengthDegreeScaler(1.0 / 3.0)
                .registerProduction('F', "F+F--F+F")
                .setAxiom("F")
                .build();

    }
}
