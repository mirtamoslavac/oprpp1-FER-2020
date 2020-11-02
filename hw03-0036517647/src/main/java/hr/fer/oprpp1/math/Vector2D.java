package hr.fer.oprpp1.math;

import java.util.Objects;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

/**
 * The {@code Vector2D} class represents the implementation of a 2D vector.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Vector2D {

    /**
     * Real part of the current 2D vector.
     */
    private double x;

    /**
     * Imaginary part of the current 2D vector.
     */
    private double y;

    /**
     * Creates a new {@code Vector2D} instance consisting of a real and imaginary part.
     *
     * @param x real part of the new vector.
     * @param y imaginary part of the new vector.
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Fetches the value of the real part of the current 2D vector.
     *
     * @return value of the real part of the current 2D vector.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Fetches the value of the imaginary part of the current 2D vector.
     *
     * @return value of the imaginary part of the current 2D vector.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Directly modifies the current 2D vector by adding an offset, represented by another 2D vector.
     *
     * @param offset a different 2D vector instance that is to be added.
     * @throws NullPointerException when the given {@code offset} is null.
     */
    public void add(Vector2D offset) {
        if (offset == null) throw new NullPointerException("The given offset cannot be null!");
        
        this.x += offset.x;
        this.y += offset.y;
    }

    /**
     * Adds an offset, represented by another 2D vector, to the current 2D vector.
     *
     * @param offset a different 2D vector instance that is to be added.
     * @throws NullPointerException when the given {@code offset} is null.
     * @return the addition result as a new {@code Vector2D} instance.
     */
    public Vector2D added(Vector2D offset) {
        if (offset == null) throw new NullPointerException("The given offset cannot be null!");    
        
        return new Vector2D(this.x + offset.x, this.y + offset.y);
    }

    /**
     * Directly modifies the current 2D vector by modifying it's angle.
     *
     * @param angle the amount by which the current 2D vector is to be rotated.
     */
    public void rotate(double angle) {
        double oldX = this.x;
        double oldY = this.y;

        this.x = oldX * cos(angle) - oldY * sin(angle);
        this.y = oldX * sin(angle) + oldY * cos(angle);
    }

    /**
     * Modifies the angle of the current 2D vector.
     * 
     * @param angle the amount by which the new 2D vector is to be rotated.
     * @return the result of the rotation as a new {@code Vector2D} instance.
     */
    public Vector2D rotated(double angle) {
        return new Vector2D(this.x * cos(angle) - this.y * sin(angle), this.x * sin(angle) + this.y * cos(angle));
    }

    /**
     * Directly modifies the current 2D vector by scaling it.
     * 
     * @param scaler a scalar by which the current 2D vector is to be scaled.
     */
    public void scale(double scaler) {
        this.x *= scaler;
        this.y *= scaler;
    }

    /**
     * Modifies the current 2D vector by scaling it.
     * 
     * @param scaler a scalar by which the new 2D vector is to be scaled.
     * @return the result of scaling as a new {@code Vector2D} instance.
     */
    public Vector2D scaled(double scaler) {
        return new Vector2D(this.x * scaler, this.y * scaler);
    }

    /**
     * Returns a new copy of the current 2D vector.
     *
     * @return a new {@code Vector2D} instance that is a copy of the current 2D vector.
     */
    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2D)) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.x, this.x) == 0 &&
                Double.compare(vector2D.y, this.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
