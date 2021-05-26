package hr.fer.oprpp1.fractals;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@code Newton} class contains a sequential, i.e. non-multi-threaded, implementation of a Newton-Raphson iteration-based fractal viewer.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Newton {

    /**
     * String that terminates the program.
     */
    private static final String TERMINATOR = "done";
    /**
     * Convergence threshold used during the calculation.
     */
    private static final double CONVERGENCE_THRESHOLD = 0.001;
    /**
     * Root threshold during the calculation.
     */
    private static final double ROOT_THRESHOLD = 0.002;
    /**
     * Maximum number of iterations allowed while determining convergence.
     */
    private static final int MAX_ITERATIONS = 16 * 16 * 16;

    /**
     * Program that serves as an executor of the sequential Newton-Raphson fractal viewer.
     *
     * @param args an array of command-line arguments.
     */
    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\nPlease enter at least two roots, one root per line. Enter 'done' when done.");
        int rootNumber = 0;
        List<Complex> roots = new ArrayList<>();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("Root " + (rootNumber + 1) + "> ");
                String next = sc.nextLine().trim();

                if (next.equalsIgnoreCase(TERMINATOR)) {
                    if (rootNumber < 2) {
                        System.out.println("At least two roots need to be given, got " + rootNumber + "!");
                        continue;
                    } else {
                        System.out.println("Image of fractal will appear shortly. Thank you.");
                        break;
                    }
                }

                if (next.isBlank()) {
                    System.out.println("The root cannot be blank!");
                    continue;
                }

                try {
                    if (!Complex.parseAndAddToList(next, roots)) {
                        System.out.println("Invalid complex number syntax!");
                        continue;
                    }
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                rootNumber++;
            }
        }

        FractalViewer.show(new NewtonFractalProducer(new ComplexRootedPolynomial(Complex.ONE, roots.toArray(Complex[]::new))));
    }

    /**
     * The {@code NewtonFractalProducer} class represents an implementation of the {@link IFractalProducer} for single-threaded production of a Newton-Raphson fractal.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private static class NewtonFractalProducer implements IFractalProducer {

        ComplexRootedPolynomial rootedPolynomial;
        ComplexPolynomial polynomial;
        ComplexPolynomial firstDerivative;

        /**
         * Creates a new {@code NewtonFractalParallelProducer}.
         *
         * @param rootedPolynomial polynomial used for production.
         */
        private NewtonFractalProducer(ComplexRootedPolynomial rootedPolynomial) {
            this.rootedPolynomial = Objects.requireNonNull(rootedPolynomial, "The given polynomial cannot be null!");
            this.polynomial = rootedPolynomial.toComplexPolynom();
            this.firstDerivative = this.polynomial.derive();
        }

        @SuppressWarnings("Duplicates")
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            int offset = 0;
            short[] data = new short[width * height];

            for(int y = 0; y < height; y++) {
                if(cancel.get()) break;
                for(int x = 0; x < width; x++) {
                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                    Complex zn = new Complex(cre, cim), znold;

                    int iter = 0;
                    do {
                        znold = zn;
                        zn = zn.sub(this.polynomial.apply(zn).divide(this.firstDerivative.apply(zn)));
                        iter++;
                    } while(zn.sub(znold).module() > CONVERGENCE_THRESHOLD && iter < MAX_ITERATIONS);

                    data[offset++] = (short)(this.rootedPolynomial.indexOfClosestRootFor(zn, ROOT_THRESHOLD) + 1);
                }
            }
            observer.acceptResult(data, (short)(polynomial.order() + 1), requestNo);
        }
    }
}
