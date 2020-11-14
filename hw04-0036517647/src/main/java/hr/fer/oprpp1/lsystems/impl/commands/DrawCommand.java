package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.lsystems.impl.TurtleState;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

/**
 * The {@code DrawCommand} class represents an implementation of the draw command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class DrawCommand implements Command {

    /**
     * The amount stating how many unit lengths should the turtle move for.
     */
    double step;

    /**
     * Creates a new {@code DrawCommand} instance.
     *
     * @param step how many unit lengths should the turtle move for.
     */
    public DrawCommand(double step) {
        this.step = step;
    }

    /**
     * Modifies the {@code TurtleState.turtlePosition} by adding the given {@code step} to it and creates a new line from the previous position to the newly calculated one.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} or {@code painter} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(painter, "The given painter cannot be null!");
        Vector2D oldPosition = Objects.requireNonNull(ctx, "The given context cannot be null!").getCurrentState().getTurtlePosition();

        new SkipCommand(step).execute(ctx, painter);

        Vector2D newPosition = ctx.getCurrentState().getTurtlePosition();

        painter.drawLine(oldPosition.getX(), oldPosition.getY(), newPosition.getX(), newPosition.getY(), ctx.getCurrentState().getDrawingColour(), 1f);
    }
}
