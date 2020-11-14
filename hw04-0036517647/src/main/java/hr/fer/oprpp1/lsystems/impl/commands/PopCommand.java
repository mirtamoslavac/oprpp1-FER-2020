package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.lsystems.impl.Command;
import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.lsystems.impl.TurtleState;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

/**
 * The {@code PopCommand} class represents an implementation of the pop command in the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class PopCommand implements Command {

    /**
     * Removes the last {@link TurtleState} from the top of the {@link ObjectStack} instance.
     *
     * @param ctx the context of the current LSystem.
     * @param painter painter that enables executing the given command.
     * @throws NullPointerException when the given {@code ctx} is {@code null}.
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Objects.requireNonNull(ctx, "The given context cannot be null!").popState();
    }
}
