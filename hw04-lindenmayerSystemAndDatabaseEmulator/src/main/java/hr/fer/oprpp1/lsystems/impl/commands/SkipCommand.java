package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.lsystems.impl.TurtleState;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

/**
 * The {@code SkipCommand} class represents an implementation of the skip command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class SkipCommand implements Command {

    /**
     * The amount stating how many unit lengths should the turtle move for.
     */
    double step;

    /**
     * Creates a new {@code DrawCommand} instance.
     *
     * @param step how many unit lengths should the turtle move for.
     */
    public SkipCommand(double step) {
        this.step = step;
    }

    /**
     * Modifies the {@code TurtleState.turtlePosition} by adding the given {@code step} to it, without leaving a trail behind.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} or {@code painter} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(painter, "The given painter cannot be null!");
        TurtleState currentState = Objects.requireNonNull(ctx, "The given context cannot be null!").getCurrentState();

        currentState.setTurtlePosition(currentState.getTurtlePosition().added(currentState.getTurtleOrientation().scaled(this.step * currentState.getEffectiveUnitLength())));
    }
}
