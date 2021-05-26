package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.lsystems.impl.TurtleState;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

/**
 * The {@code PushCommand} class represents an implementation of the push command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class PushCommand implements Command {

    /**
     * Creates a copy of the {@link TurtleState} on the top of the {@link ObjectStack} instance and pushes it on that same stack.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(ctx, "The given context cannot be null!").pushState(ctx.getCurrentState().copy());
    }
}