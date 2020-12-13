package hr.fer.oprpp1.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code ComplexPolynomial} class models a complex polynomial in the form of f(z)=zn*z^n+zn-1*z^n-1+...+z2*z^2+z1*z+z0.
 * z0 to zn represent coefficients alongside their respective powers of z.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ComplexPolynomial {

    /**
     * N + 1 polynomial coefficients.
     */
    private final Complex[] factors;

    /**
     * Creates a new immutable {@code ComplexPolynomial} instance.
     *
     * @param factors coefficients of the polynomial
     * @throws IllegalArgumentException when the number of given factors is not at least 1.
     * @throws NullPointerException when the given {@code factors} array or at leastone of of its elements is {@code null}.
     */
    public ComplexPolynomial(Complex ...factors) {
        Arrays.stream(Objects.requireNonNull(factors, "The given array of n roots cannot be null!"))
                .forEach(factor -> Objects.requireNonNull(factor, "The given complex root cannot be null!"));

        if(factors.length < 1) throw new IllegalArgumentException("The given factors array must at least contain the constant!");

        this.factors = Arrays.copyOf(factors, factors.length);
    }

    /**
     * Returns the order of the current polynomial.
     *
     * @return the order of the current polynomial.
     */
    public short order() {
        return (short)(this.factors.length - 1);
    }

    /**
     * Multiplies the current complex polynomial by the other given complex polynomial {@code c}.
     *
     * @param p complex polynomial representing the multiplier.
     * @throws NullPointerException when {@code p} is passed as {@code null}.
     * @return new {@code ComplexPolynomial} that is the product.
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Objects.requireNonNull(p, "The given polynomial of n roots cannot be null!");

        Complex[] newFactors = new Complex[this.order() + p.order() + 1];

        for (int i = 0, thisLength = this.factors.length; i < thisLength; i++) {
            for (int j = 0, pLength = p.factors.length; j < pLength; j++) {
                if (newFactors[i + j] == null) newFactors[i + j] = this.factors[i].multiply(p.factors[j]);
                else newFactors[i + j] = newFactors[i + j].add(this.factors[i].multiply(p.factors[j]));
            }
        }

        return new ComplexPolynomial(newFactors);
    }

    /**
     * Computes the first derivative of the current polynomial.
     *
     * @return the first derivative as a {@code ComplexPolynomial} instance.
     */
    public ComplexPolynomial derive() {
        if(this.factors.length == 1) return new ComplexPolynomial(Complex.ZERO);

        Complex[] newFactors = new Complex[this.factors.length - 1];
        for (int i = 1, n = this.factors.length - 1; i <= n; i++) newFactors[i - 1] = this.factors[i].multiply(new Complex(i, 0));

        return new ComplexPolynomial(newFactors);
    }


    /**
     * Computes the polynomial value at the given point {@code z}.
     *
     * @param z selected complex number.
     * @throws NullPointerException when the given {@code z} is {@code null}.
     * @return new {@code Complex} that is the result of the function.
     */
    public Complex apply(Complex z) {
        Objects.requireNonNull(z, "The given complex number cannot be null!");
        Complex product = this.factors[0];

        for (int i = 1, n = factors.length - 1; i <= n; i++) product = product.add(z.power(i).multiply(this.factors[i]));

        return product;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int i = factors.length - 1; i >= 0; i--) {
            sb.append("(").append(this.factors[i].toString()).append(")");
            if(i != 0) sb.append("*z^").append(i).append("+");
        }
        
        return sb.toString();
    }
}
