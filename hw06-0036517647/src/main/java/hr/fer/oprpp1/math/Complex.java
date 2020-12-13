package hr.fer.oprpp1.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.atan2;
import static java.lang.Math.hypot;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static java.lang.Math.PI;

import static java.lang.Double.isNaN;

/**
 * The {@code Complex} class represents an implementation of an unmodifiable complex number.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Complex {

    /**
     * Real part of the current complex number.
     */
    private final double real;

    /**
     * Imaginary part of the current complex number.
     */
    private final double imaginary;

    /**
     * Magnitude of the current complex number, also known as the distance from the origin, in its polar form.
     */
    private final double magnitude;

    /**
     * Angle of the current complex number in its polar form, also known as argument.
     */
    private final double angle;

    /**
     * Zero complex number.
     */
    public static final Complex ZERO = new Complex(0,0);
    /**
     * Positive pure real unit complex number.
     */
    public static final Complex ONE = new Complex(1,0);
    /**
     * Negative pure real unit complex number.
     */
    public static final Complex ONE_NEG = new Complex(-1,0);
    /**
     * Positive pure imaginary unit complex number.
     */
    public static final Complex IM = new Complex(0,1);
    /**
     * Negative pure imaginary unit complex number.
     */
    public static final Complex IM_NEG = new Complex(0,-1);

    /**
     * Creates a new zero complex number.
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * Creates a new {@code Complex} defined by its {@code real} and {@code imaginary} parts.
     *
     * @param re real part of the new {@code Complex}.
     * @param im imaginary part of the new {@code Complex}.
     */
    public Complex(double re, double im) {
        this.real = re;
        this.imaginary = im;
        this.magnitude = hypot(real, imaginary);
        double angleRad = atan2(imaginary, real);
        this.angle = angleRad >= 0 ? angleRad : angleRad + 2 * PI;
    }

    /**
     * Fetches the {@code magnitude} of the current complex number.
     *
     * @return magnitude of the current complex number
     */
    public double module() {
        return this.magnitude;
    }

    /**
     * Multiplies the current complex number by the other given complex number {@code c}.
     *
     * @param c complex number representing the multiplier.
     * @throws NullPointerException when {@code c} is passed as {@code null}.
     * @return new {@code Complex} that is the result of the multiplication.
     */
    public Complex multiply(Complex c) {
        Objects.requireNonNull(c,"The passed complex number cannot be null!");

        return new Complex(this.real * c.real - this.imaginary * c.imaginary, this.real * c.imaginary + this.imaginary * c.real);
    }

    /**
     * Divides the current complex number by the other given complex number {@code c}.
     *
     * @param c complex number representing the divisor.
     * @throws ArithmeticException when attempting to divide by zero.
     * @throws NullPointerException when {@code c} is passed as {@code null}.
     * @return new {@code Complex} that is the result of the division.
     */
    public Complex divide(Complex c) {
        Objects.requireNonNull(c,"The passed complex number cannot be null!");

        double divisor = c.real * c.real + c.imaginary * c.imaginary;
        if (divisor == 0) throw new ArithmeticException("Cannot divide by zero!");

        return new Complex((this.real * c.real + this.imaginary * c.imaginary) / divisor,
                (this.imaginary * c.real - this.real * c.imaginary) / divisor);
    }

    /**
     * Adds the other given complex number {@code c} to the current complex number.
     *
     * @param c complex number representing the second addend.
     * @throws NullPointerException when {@code n} is passed as {@code null}.
     * @return new {@code Complex} that is the result of the addition.
     */
    public Complex add(Complex c) {
        Objects.requireNonNull(c,"The passed complex number cannot be null!");

        return new Complex(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Subtracts the other given complex number {@code c} from the current complex number.
     *
     * @param c complex number representing the subtrahend.
     * @throws NullPointerException when {@code n} is passed as {@code null}.
     * @return new {@code Complex} that is the result of the subtraction.
     */
    public Complex sub(Complex c) {
        Objects.requireNonNull(c,"The passed complex number cannot be null!");

        return new Complex(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Negates the current complex number.
     *
     * @return  new {@code Complex} that is the negation of the current instance.
     */
    public Complex negate() {
        return new Complex(-this.real, -this.imaginary);
    }

    /**
     * Calculates the current complex number raised to the power of {@code n}.
     *
     * @param n exponent.
     * @throws IllegalArgumentException when {@code n} is less than 0.
     * @return new {@code Complex} that is the result of the expression.
     */
    public Complex power(int n) {
        if (n < 0) throw new IllegalArgumentException("Cannot raise complex numbers to the negative power of n, given n: " + n + "!");

        double magnitudeToTheNth = pow(this.magnitude, n);
        return new Complex(magnitudeToTheNth * cos(n * this.angle), magnitudeToTheNth * sin(n * this.angle));
    }

    /**
     * Calculates the {@code n} {@code n}-th complex roots of the current complex number.
     *
     * @param n degree of the root.
     * @throws IllegalArgumentException when {@code n} is not a non-natural number.
     * @return n new complex roots that are the result of the expression.
     */
    public List<Complex> root(int n) {
        if (n <= 0) throw new IllegalArgumentException("Cannot extract the root of complex numbers where n is not a non-natural number, given n: " + n + "!");

        List<Complex> complexRoots = new ArrayList<>();

        double magnitudeRoot = pow(this.magnitude, 1. / n);

        for (int k = 0; k < n; k++)
            complexRoots.add(fromMagnitudeAndAngle(magnitudeRoot, (this.angle + 2 * PI * k) / n));

        return complexRoots;
    }

    /**
     * Creates a new {@code Complex} whose real and imaginary parts are calculated based on the given {@code magnitude} and {@code angle}.
     *
     * @param  magnitude magnitude part of the new {@code Complex}.
     * @param  angle phase of the new {@code Complex}.
     * @return new {@code Complex}.
     */
    private static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
        return new Complex(magnitude * cos(angle), magnitude * sin(angle));
    }

    //TODO redo this to show the entire complex number
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if(this.real != 0 || this.imaginary == 0) {
            if(abs(this.real % 1) <= 10E-8) {
                sb.append((int)this.real);
            } else {
                sb.append(this.real);
            }
        }

        if (this.imaginary != 0) {
            if (this.imaginary == 1) {
                sb.append("+i");
            } else if (this.imaginary == -1) {
                sb.append("-i");
            } else {
                if (Math.signum(this.imaginary) == 1. && this.real != 0 || isNaN(this.imaginary)){
                    sb.append("+");
                }
                if(abs(this.imaginary % 1) <= 10E-8) {
                    sb.append((int)this.imaginary).append("i");
                } else {
                    sb.append(this.imaginary).append("i");
                }
            }
        }

        return sb.toString();
    }
}