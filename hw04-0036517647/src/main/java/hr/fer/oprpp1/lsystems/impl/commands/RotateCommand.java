package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

import static java.lang.Math.toRadians;

/**
 * The {@code RotateCommand} class represents an implementation of the rotate command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class RotateCommand implements Command {

    /**
     * Angle for which the current value of {@code TurtleState.turtleOrientation} will be rotated.
     */
    private final double angle;

    /**
     * Creates a new {@code RotateCommand} instance.
     *
     * @param angle value for which the {@code turtleOrientation} will be rotated.
     */
    public RotateCommand(double angle) {
        this.angle = angle;
    }

    /**
     * Modifies the {@code TurtleState.turtleOrientation} of the state on top of the {@code ctx}'s stack by rotating it for the given {@code angle}.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(ctx, "The given context cannot be null!").getCurrentState().getTurtleOrientation().rotate(toRadians(this.angle));
    }
}
