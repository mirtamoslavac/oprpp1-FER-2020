package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.math.Vector2D;

import java.awt.*;
import java.util.Objects;

/**
 * The {@code TurtleState} class stores the current position of the turtle within the LSystem.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class TurtleState {
    /**
     * The current location of the turtle.
     */
    Vector2D turtlePosition;

    /**
     * The normalized current orientation of the turtle.
     */
    Vector2D turtleOrientation;

    /**
     * The colour of the turtle's trail.
     */
    Color drawingColour;

    /**
     * Unit length of the turtle's movement.
     */
    double effectiveUnitLength;

    /**
     * Creates a new {@code TurtleState} instance.
     *
     * @param turtlePosition current location of the turtle.
     * @param turtleOrientation current orientation of the turtle.
     * @param drawingColour colour of the turtle's trail.
     * @param effectiveUnitLength unit length of the turtle's movement.
     */
    public TurtleState(Vector2D turtlePosition, Vector2D turtleOrientation, Color drawingColour, double effectiveUnitLength) {
        this.turtlePosition = Objects.requireNonNull(turtlePosition, "The given turtle position cannot be null!");
        this.turtleOrientation = Objects.requireNonNull(turtleOrientation, "The given turtle orientation cannot be null!");
        this.drawingColour = Objects.requireNonNull(drawingColour, "The given drawing colour cannot be null!");
        this.effectiveUnitLength = effectiveUnitLength;
    }

    /**
     * Stores a new position for the current {@code TurtleState} instance.
     *
     * @param turtlePosition the new {@code turtlePosition} of the current state.
     */
    public void setTurtlePosition(Vector2D turtlePosition) {
        this.turtlePosition = Objects.requireNonNull(turtlePosition, "The given turtle position cannot be null!");
    }

    /**
     * Stores a new orientation for the current {@code TurtleState} instance.
     *
     * @param turtleOrientation the new {@code turtleOrientation} of the current state.
     */
    public void setTurtleOrientation(Vector2D turtleOrientation) {
        this.turtleOrientation = Objects.requireNonNull(turtleOrientation, "The given turtle orientation cannot be null!");
    }

    /**
     * Stores a new drawingColour for the current {@code TurtleState} instance.
     *
     * @param drawingColour the new {@code drawingColour} of the current state.
     */
    public void setDrawingColour(Color drawingColour) {
        this.drawingColour = Objects.requireNonNull(drawingColour, "The given drawing colour cannot be null!");
    }

    /**
     * Stores a new effectiveUnitLength for the current {@code TurtleState} instance.
     *
     * @param effectiveUnitLength the new {@code effectiveUnitLength} of the current state.
     */
    public void setEffectiveUnitLength(double effectiveUnitLength) {
        this.effectiveUnitLength = effectiveUnitLength;
    }

    /**
     * Fetches the current location of the current {@code TurtleState} instance.
     *
     * @return {@link Vector2D} instance representing the {@code turtlePosition} of the current state.
     */
    public Vector2D getTurtlePosition() {
        return this.turtlePosition;
    }

    /**
     * Fetches the current orientation of the current {@code TurtleState} instance.
     *
     * @return {@link Vector2D} instance representing the {@code turtleOrientation} of the current state.
     */
    public Vector2D getTurtleOrientation() {
        return this.turtleOrientation;
    }

    /**
     * Fetches the current trail colour of the current {@code TurtleState} instance.
     *
     * @return {@link Color} instance representing the {@code drawingColour} of the current state.
     */
    public Color getDrawingColour() {
        return this.drawingColour;
    }

    /**
     * Fetches the current effective unit length of the current {@code TurtleState} instance.
     *
     * @return a primitive double representing the {@code effectiveUnitLength} of the current state.
     */
    public double getEffectiveUnitLength() {
        return this.effectiveUnitLength;
    }

    /**
     * Creates a new copy of the current {@code TurtleState} instance.
     *
     * @return new {@code TurtleState} instance.
     */
    public TurtleState copy() {
        return new TurtleState(this.turtlePosition.copy(), this.turtleOrientation.copy(), this.drawingColour, this.effectiveUnitLength);
    }
}
