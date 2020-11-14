package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.lsystems.impl.TurtleState;
import hr.fer.zemris.lsystems.Painter;

import java.awt.*;
import java.util.Objects;

/**
 * The {@code ColorCommand} class represents an implementation of the color command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ColorCommand implements Command {

    /**
     * The colour of the turtle's trail.
     */
    Color colour;

    /**
     * Creates a new {@code DrawCommand} instance.
     *
     * @param colour the new colour of the turtle's trail.
     */
    public ColorCommand(Color colour) {
        this.colour = Objects.requireNonNull(colour, "The given colour cannot be null!");
    }

    /**
     * Changes the {@code TurtleState.drawingColour}.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(ctx, "The given context cannot be null!").getCurrentState().setDrawingColour(this.colour);
    }
}
