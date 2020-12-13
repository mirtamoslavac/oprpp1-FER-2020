package hr.fer.oprpp1.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code ComplexRootedPolynomial} class models a complex polynomial in the form of f(z)=z0*(z-z1)*(z-z2)*...*(z-zn).
 * z1 to zn represent the roots of the polynomial, while z0 is its constant.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ComplexRootedPolynomial {

    /**
     * Constant of the polynomial.
     */
    private final Complex constant;

    /**
     * N polynomial complex roots.
     */
    private final Complex[] roots;

    /**
     * Creates an immutable rooted polynomial.
     *
     * @param constant constant complex number of the polynomial.
     * @param roots n polynomial complex roots
     * @throws NullPointerException when the given {@code constant}, {@code roots} or any root witin the {@code roots} array is {@code null}.
     */
    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        Objects.requireNonNull(constant, "The given complex number cannot be null!");
        Arrays.stream(Objects.requireNonNull(roots, "The given array of n roots cannot be null!"))
                .forEach(root -> Objects.requireNonNull(root, "The given complex root cannot be null!"));

        this.constant = constant;
        this.roots = Arrays.copyOf(roots, roots.length);
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
        Complex product = this.constant;

        for (Complex root : this.roots) product = z.sub(root).multiply(product);

        return product;
    }

    /**
     * Converts the current {@code ComplexRootedPolynomial} instance to {@link ComplexPolynomial} type.
     *
     * @return {@link ComplexPolynomial} instance from the current {@code ComplexRootedPolynomial} instance.
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial product = new ComplexPolynomial(this.constant);

        for (Complex root : this.roots) product = product.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));

        return  product;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(this.constant.toString()).append(")");
        Arrays.stream(this.roots).forEach(s -> sb.append("*(z-(").append(s.toString()).append("))"));

        return sb.toString();
    }

    /**
     * Finds the index of the closest root for the given complex number {@code z} that is within the given {@code threshold}.
     *
     * @param z complex number
     * @param threshold max distance allowed between a certain root and the given {@code z}.
     * @throws NullPointerException when the given {@code z} is {@code null}.
     * @return index of the closest root, -1 if there is no such root.
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        Objects.requireNonNull(z, "The given complex number cannot be null!");

        int index = -1;
        double distance = 0;

        for (int i = 0, length = this.roots.length; i < length; i++) {
            if(i == 0) {
                distance = z.sub(this.roots[i]).module();
                continue;
            }

            double newDistance = z.sub(this.roots[i]).module();
            if(newDistance < distance) {
                index = i;
                distance = newDistance;
            }
        }

        return distance < threshold && index != -1 ? index : -1;
    }
}