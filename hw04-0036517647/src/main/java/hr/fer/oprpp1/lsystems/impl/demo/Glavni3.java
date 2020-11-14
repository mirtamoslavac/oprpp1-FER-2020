package hr.fer.oprpp1.lsystems.impl.demo;

import hr.fer.oprpp1.lsystems.impl.LSystemBuilderImpl;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.gui.LSystemViewer;

/**
 * The {@code Glavni3} class is a program for creating a various fractals through text configuration.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Glavni3 {
    /**
     * Method taken from this homework's PDF dedicated to demonstrating {@link LSystem} capabilities.
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        LSystemViewer.showLSystem(LSystemBuilderImpl::new);
    }

}
