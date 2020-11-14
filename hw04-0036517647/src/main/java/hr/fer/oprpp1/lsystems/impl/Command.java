package hr.fer.oprpp1.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * The {@code Command} interface represents a model for possible commands of a certain LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes the command.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     */
    void execute(Context ctx, Painter painter);
}
