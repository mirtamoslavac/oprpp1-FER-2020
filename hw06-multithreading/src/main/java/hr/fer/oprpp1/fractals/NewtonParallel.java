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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Integer.parseInt;

/**
 * The {@code NewtonParallel} class contains a parallel, i.e. multi-threaded, implementation of a Newton-Raphson iteration-based fractal viewer.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class NewtonParallel {
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
     * Longer parameter specifier for the number of workers.
     */
    private static final String PARAMETER1_DOUBLE_HYPHEN = "--workers=";
    /**
     * Shorter parameter specifier for the number of workers.
     */
    private static final String PARAMETER1_SINGLE_HYPHEN = "-w";
    /**
     * Longer parameter specifier for the number of tracks.
     */
    private static final String PARAMETER2_DOUBLE_HYPHEN = "--tracks=";
    /**
     * Shorter parameter specifier for the number of tracks.
     */
    private static final String PARAMETER2_SINGLE_HYPHEN = "-t";

    /**
     * Program that serves as an executor of the parallel Newton-Raphson fractal viewer.
     *
     * @param args an array of command-line arguments.
     */
    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
        Integer workers = null, tracks = null;

        try {
            for (int i = 0, length = args.length; i < length; i++) {
                if (args[i].startsWith(PARAMETER1_DOUBLE_HYPHEN)) {
                    if (workers == null) workers = parseInt(args[i].substring(PARAMETER1_DOUBLE_HYPHEN.length()));
                    else throwArgumentException(PARAMETER1_DOUBLE_HYPHEN);
                } else if (args[i].equals(PARAMETER1_SINGLE_HYPHEN)){
                    if (workers == null) workers = parseInt(args[++i]);
                    else throwArgumentException(PARAMETER1_DOUBLE_HYPHEN);
                } else if (args[i].startsWith(PARAMETER2_DOUBLE_HYPHEN)) {
                    if (tracks == null) tracks = parseInt(args[i].substring(PARAMETER2_DOUBLE_HYPHEN.length()));
                    else throwArgumentException(PARAMETER2_DOUBLE_HYPHEN);
                } else if (args[i].equals(PARAMETER2_SINGLE_HYPHEN)){
                    if (tracks == null) tracks = parseInt(args[++i]);
                    else throwArgumentException(PARAMETER2_DOUBLE_HYPHEN);
                } else throw new IllegalArgumentException("Invalid parameter \""+ args[i] + "\"!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter value: " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Missing parameter value!");
            return;
        }

        if (workers == null) workers = Runtime.getRuntime().availableProcessors();
        if (tracks == null) tracks = Runtime.getRuntime().availableProcessors() * 4;

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\nPlease enter at least two roots, one root per line. Enter 'done' when done.");
        System.out.println("The number of threads (workers) is " + workers + ".");
        System.out.println("The number of tracks is " + tracks + ".");
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

        FractalViewer.show(new NewtonFractalParallelProducer(new ComplexRootedPolynomial(Complex.ONE, roots.toArray(Complex[]::new)), workers, tracks));
    }

    private static void throwArgumentException(String parameter) {
        throw new IllegalArgumentException("Cannot specify the same parameter \"" + parameter.substring(2, parameter.length() - 1) + "\" twice!");
    }

    /**
     * The {@code Work} class represents an implementation of the {@link Runnable} that calculates the colours of a certain track.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    @SuppressWarnings("Duplicates")
    public static class Work implements Runnable {
        private double reMin, reMax, imMin, imMax;
        private int width, height, yMin, yMax, maxIter;
        private short[] data;
        private AtomicBoolean cancel;
        /**
         *
         */
        public final static Work NO_JOB = new Work();

        private ComplexRootedPolynomial rootedPolynomial;
        private ComplexPolynomial polynomial;
        private ComplexPolynomial firstDerivative;

        /**
         * Creates empty {@code Work} instance.
         */
        private Work() {
        }

        /**
         * Creates a {@code Work} instance with respective parameters.
         *
         * @param reMin minimum set value on the real axis.
         * @param reMax maximum set value on the real axis.
         * @param imMin minimum set value on the imaginary axis.
         * @param imMax maximum set value on the imaginary axis.
         * @param width width of the viewer.
         * @param height height of the viewer.
         * @param yMin minimum height from which to calculate.
         * @param yMax maximum height up to which to calculate.
         * @param m maximum number of iterations.
         * @param data array containing short values that correspond to certain colour values.
         * @param cancel determines whether the calculation is over.
         * @param rootedPolynomial polynomial used for calculation.
         * @throws NullPointerException when the given {@code data}, {@code cancel} or {@code rootedPolynomial} are {@code null}.
         */
        public Work(double reMin, double reMax, double imMin,
                             double imMax, int width, int height, int yMin, int yMax,
                             int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial rootedPolynomial) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.maxIter = m;
            this.data = Objects.requireNonNull(data, "The given data array cannot be null!");
            this.cancel = Objects.requireNonNull(cancel, "The given cancel cannot be null!");
            this.rootedPolynomial = Objects.requireNonNull(rootedPolynomial, "The given rooted polynomial cannot be null!");
            this.polynomial = rootedPolynomial.toComplexPolynom();
            this.firstDerivative = polynomial.derive();
        }

        @Override
        public void run() {
            int offset = this.yMin * this.width;
            for(int y = this.yMin; y <= this.yMax; y++) {
                if(this.cancel.get()) break;
                for(int x = 0; x < this.width; x++) {
                    double cre = x / (this.width - 1.0) * (this.reMax - this.reMin) + this.reMin;
                    double cim = (this.height - 1.0 - y) / (this.height - 1) * (this.imMax - this.imMin) + this.imMin;

                    Complex zn = new Complex(cre, cim), znold;

                    int iter = 0;
                    do {
                        znold = zn;
                        zn = zn.sub(this.polynomial.apply(zn).divide(this.firstDerivative.apply(zn)));
                        iter++;
                    } while(zn.sub(znold).module() > CONVERGENCE_THRESHOLD && iter < this.maxIter);

                    this.data[offset++] = (short)(this.rootedPolynomial.indexOfClosestRootFor(zn, ROOT_THRESHOLD) + 1);
                }
            }
        }
    }

    /**
     * The {@code NewtonFractalParallelProducer} class represents an implementation of the {@link IFractalProducer} for multi-threaded production of a Newton-Raphson fractal.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    public static class NewtonFractalParallelProducer implements IFractalProducer {
        private final ComplexRootedPolynomial rootedPolynomial;
        private final int numberOfWorkers;
        private int numberOfTracks;

        /**
         * Creates a new {@code NewtonFractalParallelProducer}.
         *
         * @param rootedPolynomial polynomial used for production.
         * @param workers number of threads that will perform the calculation during production.
         * @param tracks number of tracks to which the visible frame is divided.
         */
        private NewtonFractalParallelProducer(ComplexRootedPolynomial rootedPolynomial, Integer workers, Integer tracks) {
            this.rootedPolynomial = Objects.requireNonNull(rootedPolynomial, "The given polynomial cannot be null!");
            this.numberOfWorkers = Objects.requireNonNull(workers, "The given number of workers cannot be null!");
            this.numberOfTracks = Objects.requireNonNull(tracks, "The given number of tracks cannot be null!");
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            short[] data = new short[width * height];
            if (numberOfTracks > height) numberOfTracks = height;
            int numberOfYPerTrack = height / numberOfTracks;

            final BlockingQueue<Work> queue = new LinkedBlockingQueue<>();

            Thread[] workers = new Thread[this.numberOfWorkers];
            for(int i = 0; i < workers.length; i++) {
                workers[i] = new Thread(() -> {
                    while(true) {
                        Work p;
                        try {
                            p = queue.take();
                            if(p == Work.NO_JOB) break;
                        } catch (InterruptedException e) {
                            continue;
                        }
                        p.run();
                    }
                });
            }
            for (Thread thread : workers) thread.start();

            for(int i = 0; i < this.numberOfTracks; i++) {
                int yMin = i * numberOfYPerTrack;
                int yMax = (i + 1) * numberOfYPerTrack - 1;
                if(i == this.numberOfTracks -1) yMax = height-1;
                Work work = new Work(reMin, reMax, imMin, imMax, width, height, yMin, yMax, MAX_ITERATIONS, data, cancel, this.rootedPolynomial);
                while(true) {
                    try {
                        queue.put(work);
                        break;
                    } catch (InterruptedException ignored) {}
                }
            }
            for(int i = 0; i < workers.length; i++) {
                while(true) {
                    try {
                        queue.put(Work.NO_JOB);
                        break;
                    } catch (InterruptedException ignored) {}
                }
            }

            for (Thread worker : workers) {
                while (true) {
                    try {
                        worker.join();
                        break;
                    } catch (InterruptedException ignored) {}
                }
            }

            observer.acceptResult(data, (short)(rootedPolynomial.toComplexPolynom().order() + 1), requestNo);
        }
    }
}

