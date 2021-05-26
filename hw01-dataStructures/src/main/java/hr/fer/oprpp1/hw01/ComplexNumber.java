package hr.fer.oprpp1.hw01;

import static java.lang.Math.atan2;
import static java.lang.Math.hypot;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static java.lang.Math.PI;

import static java.lang.Double.parseDouble;
import static java.lang.Double.isNaN;

/**
 * The {@code ComplexNumber} class represents an implementation of an unmodifiable complex number.
 *
 * @author mirtamoslavac
 * @version 1.1
 */
public class ComplexNumber {

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
     * Creates a new {@code ComplexNumber} defined by its {@code real} and {@code imaginary} parts.
     *
     * @param real real part of the new {@code ComplexNumber}.
     * @param imaginary imaginary part of the new {@code ComplexNumber}.
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
        this.magnitude = hypot(real, imaginary);
        double angleRad = atan2(imaginary, real);
        this.angle = angleRad >= 0 ? angleRad : angleRad + 2 * PI;
    }

    /**
     * Creates a new {@code ComplexNumber} defined just by its {@code real} part.
     *
     * @param real non-zero part of the new {@code ComplexNumber}.
     * @return new {@code ComplexNumber}.
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0.);
    }

    /**
     * Creates a new {@code ComplexNumber} defined just by its {@code imaginary} part.
     *
     * @param imaginary non-zero part of the new {@code ComplexNumber}.
     * @return new {@code ComplexNumber}.
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0., imaginary);
    }

    /**
     * Creates a new {@code ComplexNumber} whose real and imaginary parts are calculated based on the given {@code magnitude} and {@code phase}.
     *
     * @param  magnitude magnitude part of the new {@code ComplexNumber}.
     * @param  angle phase of the new {@code ComplexNumber}.
     * @return new {@code ComplexNumber}.
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        return new ComplexNumber(magnitude * cos(angle), magnitude * sin(angle));
    }

    /**
     * Parses the real and imaginary parts of the current complex number from the given string {@code s} into a new {@code ComplexNumber}.
     *
     * @param s string that is to be parsed into a {@code ComplexNumber}.
     * @throws NullPointerException when the given string {@code s} is empty.
     * @return new {@code ComplexNumber} parsed from the given string.
     */
    public static ComplexNumber parse(String s) {
        if (s == null) {
            throw new NullPointerException("The string that is to be parsed into a complex number shouldn't be empty!");
        }

        String regexRealAndImaginary = "^(([-+])?((\\d+|\\d*\\.?\\d+)|NaN|Infinity))(([-+])((\\d*\\.?\\d*)|NaN|Infinity)i)$";
        String regexReal = "^([-+])?((\\d+|\\d*\\.?\\d+)|NaN|Infinity)$";
        String regexImaginary = "^([-+])?((\\d*\\.?\\d*)|NaN|Infinity)i$";

        if (s.matches(regexRealAndImaginary)) {
            boolean negativeReal = false;
            if (s.indexOf("+") == 0) {
                s = s.substring(1);
            }
            if (s.indexOf("-") == 0) {
                s = s.substring(1);
                negativeReal = true;
            }

            s = s.substring(0, s.length() - 1);

            int operatorIndex;
            if (s.contains("+")) {
                operatorIndex = s.indexOf("+");
            } else {
                operatorIndex = s.indexOf("-");
            }

            double real = !negativeReal ? parseDouble(s.substring(0, operatorIndex)) : parseDouble("-" + s.substring(0, operatorIndex));

            double imaginary = s.substring(operatorIndex).equals("+") ? 1 : s.substring(operatorIndex).equals("-") ? -1 : parseDouble(s.substring(operatorIndex));

            return new ComplexNumber(real, imaginary);
        } else if (s.matches(regexReal)) {
                return new ComplexNumber(parseDouble(s), 0.);
        } else if (s.matches(regexImaginary)) {
            switch (s) {
                case "i", "+i" -> {
                    return new ComplexNumber(0., 1.);
                }
                case "-i" -> {
                    return new ComplexNumber(0., -1.);
                }
                default -> {
                    return new ComplexNumber(0., parseDouble(s.substring(0, s.length() - 1)));
                }
            }
        } else {
            throw new IllegalArgumentException("The given expression \"" + s + "\" cannot be parsed into a complex number!");
        }

    }

    /**
     * Fetches the {@code real} part of the current complex number.
     *
     * @return real part of the current complex number.
     */
    public double getReal() {
        return this.real;
    }

    /**
     * Fetches the {@code imaginary} part of the current complex number.
     *
     * @return imaginary part of the current complex number.
     */
    public double getImaginary() {
        return this.imaginary;
    }

    /**
     * Fetches the {@code magnitude} of the current complex number.
     *
     * @return magnitude of the current complex number
     */
    public double getMagnitude() {
        return this.magnitude;
    }

    /**
     * Fetches the {@code angle}(phase) of the current complex number.
     *
     * @return phase of the current complex number
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * Adds the other given complex number {@code c} to the current complex number.
     *
     * @param c complex number representing the second addend.
     * @throws NullPointerException when {@code n} is passed as {@code null}.
     * @return new {@code ComplexNumber} that is the result of the addition.
     */
    public ComplexNumber add(ComplexNumber c) {
        if (c == null) throw new NullPointerException("The passed complex number cannot be null!");
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Subtracts the other given complex number {@code c} from the current complex number.
     *
     * @param c complex number representing the subtrahend.
     * @throws NullPointerException when {@code n} is passed as {@code null}.
     * @return new {@code ComplexNumber} that is the result of the subtraction.
     */
    public ComplexNumber sub(ComplexNumber c) {
        if (c == null) throw new NullPointerException("The passed complex number cannot be null!");
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies the current complex number by the other given complex number {@code c}.
     *
     * @param c complex number representing the multiplier.
     * @throws NullPointerException when {@code c} is passed as {@code null}.
     * @return new {@code ComplexNumber} that is the result of the multiplication.
     */
    public ComplexNumber mul(ComplexNumber c) {
        if (c == null) throw new NullPointerException("The passed complex number cannot be null!");
        return new ComplexNumber(this.real * c.real - this.imaginary * c.imaginary, this.real * c.imaginary + this.imaginary * c.real);
    }

    /**
     * Divides the current complex number by the other given complex number {@code c}.
     *
     * @param c complex number representing the divisor.
     * @throws ArithmeticException when attempting to divide by zero.
     * @throws NullPointerException when {@code c} is passed as {@code null}.
     * @return new {@code ComplexNumber} that is the result of the division.
     */
    public ComplexNumber div(ComplexNumber c) throws ArithmeticException {
        if (c == null) throw new NullPointerException("The passed complex number cannot be null!");
        double divisor = c.real * c.real + c.imaginary * c.imaginary;
        if (divisor == 0) {
            throw new ArithmeticException("Cannot divide by zero!");
        }
        return new ComplexNumber((this.real * c.real + this.imaginary * c.imaginary) / divisor,
                             (this.imaginary * c.real - this.real * c.imaginary) / divisor);
    }

    /**
     * Calculates the current complex number raised to the power of {@code n}.
     *
     * @param n exponent.
     * @throws IllegalArgumentException when {@code n} is less than 0.
     * @return new {@code ComplexNumber} that is the result of the expression.
     */
    public ComplexNumber power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot raise complex numbers to the negative power of n, given n: " + n + "!");
        }
        double magnitudeToTheNth = pow(this.magnitude, n);
        return new ComplexNumber(magnitudeToTheNth * cos(n * this.angle), magnitudeToTheNth * sin(n * this.angle));
    }

    /**
     * Calculates the {@code n} {@code n}-th complex roots of the current complex number.
     *
     * @param n degree of the root.
     * @throws IllegalArgumentException when {@code n} is not a non-natural number.
     * @return n new complex roots that are the result of the expression.
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Cannot extract the root of complex numbers where n is not a non-natural number, given n: " + n + "!");
        }
        ComplexNumber[] complexNumberRoots = new ComplexNumber[n];

        double magnitudeRoot = pow(this.magnitude, 1. / n);

        for (int k = 0; k < n; k++) {
            complexNumberRoots[k] = fromMagnitudeAndAngle(magnitudeRoot, (this.angle + 2 * PI * k) / n);
        }

        return complexNumberRoots;
    }

    /**
     * Creates a string representation of the current complex number.
     *
     * @return string representation of the current complex number.
     */
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
