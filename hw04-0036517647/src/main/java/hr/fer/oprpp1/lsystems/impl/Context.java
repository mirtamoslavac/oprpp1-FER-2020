package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.custom.collections.ObjectStack;

import java.util.Objects;

/**
 * The {@code Context} class provides a {@link ObjectStack} instance of {@link TurtleState} instances for a certain LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Context {
    /**
     * An {@link ObjectStack} instance used for storing {@link TurtleState} instances and providing context for the current LSystem.
     */
    private final ObjectStack<TurtleState> stack;

    /**
     * Default constructor that creates a new {@code Context} instance, along with its own {@link ObjectStack} instance.
     */
    public Context() {
        this.stack = new ObjectStack<>();
    }

    /**
     * Provides the last state pushed on the stack, without removing it.
     *
     * @return {@link TurtleState} instance that is the first value on the top of the stack.
     */
    public TurtleState getCurrentState() {
        return this.stack.peek();
    }

    /**
     * Pushes the given {@code state} on the top of the stack.
     *
     * @param state {@link TurtleState} instance that is to be put on the top of the stack.
     * @throws NullPointerException when the given {@code state} is null.
     */
    public void pushState(TurtleState state) {
        this.stack.push(Objects.requireNonNull(state, "The given turtle state cannot be null!"));
    }

    /**
     * Removes last pushed {@link TurtleState} instance from stack.
     */
    public void popState() {
        this.stack.pop();
    }
}
