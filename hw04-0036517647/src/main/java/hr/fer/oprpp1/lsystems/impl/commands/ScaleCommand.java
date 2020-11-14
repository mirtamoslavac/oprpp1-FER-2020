package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

/**
 * The {@code ScaleCommand} class represents an implementation of the scale command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ScaleCommand implements Command {

    /**
     * Scale factor for which the current value of {@code TurtleState.effectiveUnitLength} will be scaled.
     */
    private final double factor;

    /**
     * Creates a new {@code ScaleCommand} instance.
     *
     * @param factor value for which the {@code effectiveUnitLength} will be scaled.
     */
    public ScaleCommand(double factor) {
        this.factor = factor;
    }

    /**
     * Modifies the {@code TurtleState.effectiveUnitLength} of the state on top of the {@code ctx}'s stack by scaling it by the given {@code factor}.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(ctx, "The given context cannot be null!").getCurrentState().setEffectiveUnitLength(this.factor * ctx.getCurrentState().getEffectiveUnitLength());
    }
}

