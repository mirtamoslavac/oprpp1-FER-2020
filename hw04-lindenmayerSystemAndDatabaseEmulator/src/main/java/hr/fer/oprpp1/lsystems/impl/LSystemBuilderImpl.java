package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.lsystems.impl.commands.*;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;

import static java.lang.Double.*;
import static java.lang.Math.*;

import java.awt.*;
import java.util.Objects;

/**
 * The {@code LSystemBuilderImpl} class represents an implementation of a {@link LSystemBuilder}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LSystemBuilderImpl implements LSystemBuilder {
    /**
     * Current unit length size of the current LSystem.
     */
    private double unitLength;

    /**
     * Current scaler of the current LSystem.
     */
    private double unitLengthDegreeScaler;

    /**
     * Current origin point of the current LSystem.
     */
    private Vector2D origin;

    /**
     * Current angle of the current LSystem.
     */
    private double angle;

    /**
     * Axiom of the current LSystem.
     */
    private String axiom;

    /**
     * Default colour for all LSystems.
     */
    private static final Color DEFAULT_COLOUR = Color.BLACK;

    /**
     * Stores registered productions in the current LSystem.
     */
    private final Dictionary<Character, String> registeredProductions;

    /**
     * Stores registered commands in the current LSystem.
     */
    private final Dictionary<Character, Command> registeredCommands;

    /**
     * Default constructor that creates a new {@code LSystemBuilderImpl} instance with implied values.
     */
    public LSystemBuilderImpl() {
        this.unitLength = 0.1;
        this.unitLengthDegreeScaler = 1;
        this.origin = new Vector2D(0, 0);
        this.angle = 0;
        this.axiom = "";
        this.registeredProductions = new Dictionary<>();
        this.registeredCommands = new Dictionary<>();
    }

    /**
     * Sets the unit length for the current LSystem instance.
     *
     * @param unitLength value of the new unit length.
     * @throws IllegalArgumentException when the given {@code unitLength} is non-positive, infinite or not a number.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder setUnitLength(double unitLength) {
        if (unitLength <= 0 || isInfinite(unitLength) || isNaN(unitLength)) throw new IllegalArgumentException("The given unitLength cannot be a non-positive infinite number, or not a number in general.");

        this.unitLength = unitLength;

        return this;
    }

    /**
     * Sets the origin point for the current LSystem instance.
     *
     * @param x abscissa value of the origin point.
     * @param y ordinate value of the origin point.
     * @throws IllegalArgumentException when the given {@code x} or {@code x} are negative, infinite or not a number.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder setOrigin(double x, double y) {
        if (x < 0 || isInfinite(x) || isNaN(x)) throw new IllegalArgumentException("The given x of the origin cannot be a non-positive infinite number, or not a number in " +
                "general!");
        if (y < 0 || isInfinite(y) || isNaN(y)) throw new IllegalArgumentException("The given y of the origin cannot be a non-positive infinite number, or not a number in " +
                "general!");

        this.origin = new Vector2D(x, y);

        return this;
    }

    /**
     * Sets the angle for the current LSystem instance.
     *
     * @param angle value of the new angle.
     * @throws IllegalArgumentException when the given {@code angle} infinite or not a number.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder setAngle(double angle) {
        if (isInfinite(angle) || isNaN(angle)) throw new IllegalArgumentException("The given angle cannot be an infinite number or not a number!");

        this.angle = angle;

        return this;
    }

    /**
     * Sets the axiom for the current LSystem instance.
     *
     * @param axiom axiom for the LSystem.
     * @throws NullPointerException when the given {@code axiom} is {@code null}.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = Objects.requireNonNull(axiom, "The given axiom cannot be null!");

        return this;
    }

    /**
     * Sets the unit length scaler, in degrees, for the current LSystem instance.
     *
     * @param unitLengthDegreeScaler scaler for the unit length of the current LSystem.
     * @throws IllegalArgumentException when the given {@code angle} non-positive, infinite or not a number.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        if (unitLengthDegreeScaler <= 0 || isInfinite(unitLengthDegreeScaler) || isNaN(unitLengthDegreeScaler)) throw new IllegalArgumentException("The given unit length degree scaler cannot be a non-positive infinite number, or not a " +
                "number in general!");

        this.unitLengthDegreeScaler = unitLengthDegreeScaler;

        return this;
    }

    /**
     * Registers a new production for the LSystem.
     *
     * @param symbol character on the left-hand side of the production.
     * @param production right-hand side string of the production.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder registerProduction(char symbol, String production) {
        if (this.registeredProductions.get(symbol) != null) throw new IllegalArgumentException("Cannot define multiple productions for one and the same symbol!");

        this.parseProduction(symbol, Objects.requireNonNull(production, "The given production cannot be null!"));

        return this;
    }

    /**
     * Registers a new command for the LSystem.
     *
     * @param symbol character representing that command.
     * @param action action connected to that command.
     * @return current {@code LSystemBuilder} instance.
     */
    @Override
    public LSystemBuilder registerCommand(char symbol, String action) {

        this.parseCommand(symbol, Objects.requireNonNull(action, "The given command cannot be null!"));

        return this;
    }

    /**
     * Creates a new {@link LSystemBuilder} configuration from {@code lines}.
     *
     * @param lines text rows that are to be parsed into a {@link LSystemBuilder} instance.
     * @throws IllegalArgumentException when unable to configure the {@link LSystemBuilder} instance from the information given.
     * @throws NullPointerException when the given {@code lines} is {@code null}.
     * @throws NumberFormatException when unable to parse a string to a decimal type.
     * @return {@link LSystemBuilder} instance configured from the given {@code lines}.
     */
    @Override
    public LSystemBuilder configureFromText(String[] lines) {
        Objects.requireNonNull(lines, "The given lines cannot be null!");

        for (String line : lines) {
            String[] elements = line.trim().split("\\s+");

            if (elements.length == 0 || elements[0].equals("")) continue;

            switch (elements[0].toLowerCase()) {
                case "origin" -> {
                    if (elements.length != 3) throw new IllegalArgumentException("Incorrect amount of information for setting the origin!");

                    this.setOrigin(parseDouble(elements[1]), parseDouble(elements[2]));
                }
                case "angle" -> {
                    if (elements.length != 2) throw new IllegalArgumentException("Incorrect amount of information for setting the angle!");

                    this.setAngle(parseDouble(elements[1]));
                }
                case "unitlength" -> {
                    if (elements.length != 2) throw new IllegalArgumentException("Incorrect amount of information for setting the unit length!");

                    this.setUnitLength(parseDouble(elements[1]));
                }
                case "unitlengthdegreescaler" -> {
                    if (elements.length != 2 && elements.length !=3 && elements.length !=4) throw new IllegalArgumentException("Incorrect amount of information for setting the " +
                            "unit length degree scaler!");

                    if (elements.length == 2) {
                        if(!elements[1].contains("/")) this.setUnitLengthDegreeScaler(parseDouble(elements[1]));
                        else {
                            String[] operands = elements[1].split("/");
                            this.setUnitLengthDegreeScaler(parseDouble(operands[0]) / parseDouble(operands[1]));
                        }
                    }
                    else if (elements.length == 3){
                        double operand1, operand2;
                        if (elements[1].contains("/")) {
                            operand1 = parseDouble(elements[1].substring(0, elements[1].length()-1));
                            operand2 = parseDouble(elements[2]);
                        } else {
                            operand1 = parseDouble(elements[1]);
                            operand2 = parseDouble(elements[2].substring(1, elements[1].length()));
                        }

                        this.setUnitLengthDegreeScaler(operand1 / operand2);
                    } else {
                        if (!elements[2].equals("/")) throw new IllegalArgumentException("Invalid operation within the calculation of the unit length degree scaler!");
                        this.setUnitLengthDegreeScaler(parseDouble(elements[1]) / parseDouble(elements[3]));
                    }
                }
                case "command" -> this.storeCommand(elements);
                case "axiom" -> {
                    if (elements.length != 2) throw new IllegalArgumentException("Incorrect amount of information for setting the axiom!");

                    this.setAxiom(elements[1]);
                }
                case "production" -> {
                    if (elements.length != 3) throw new IllegalArgumentException("Incorrect amount of information for setting the production!");

                    this.registeredProductions.put(elements[1].charAt(0), elements[2]);
                }
                default -> throw new IllegalArgumentException("Invalid command \"" + elements[0] + "\"!");
            }
        }

        return this;
    }

    /**
     * Creates a base point for drawing a new LSystem.
     *
     * @return {@link LSystem} instance.
     */
    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    /**
     * Adapts the production not given as a line of text to such and stores it.
     *
     * @param symbol character on the left-hand side of the production.
     * @param production right-hand side string of the production.
     * @throws NullPointerException when the given {@code production} is null.
     */
    private void parseProduction(char symbol, String production) {
        this.storeProduction(this.createRHSArray(symbol, production, "production"));
    }

    /**
     * Stores the production.
     *
     * @param elements array containing necessary information for storing a production.
     */
    private void storeProduction(String[] elements) {
        this.registeredProductions.put(elements[1].charAt(0), elements[2]);
    }


    /**
     * Adapts the command not given as a line of text to such and stores it.
     *
     * @param symbol character representing that command.
     * @param command action connected to that command.
     * @throws NullPointerException when the given {@code command} is null.
     */
    private void parseCommand(char symbol, String command) {
        this.storeCommand(this.createRHSArray(symbol, command, "command"));
    }

    /**
     * Stores the command.
     *
     * @param elements array containing necessary information for storing a command.
     * @throws NullPointerException when the given {@code elements} is null.
     */
    private void storeCommand(String[] elements) {
        Objects.requireNonNull(elements, "The given array cannot be null!");
        switch (elements[2].toLowerCase()) {
            case "draw" -> this.registeredCommands.put(elements[1].charAt(0), new DrawCommand(parseDouble(elements[3])));
            case "skip" -> this.registeredCommands.put(elements[1].charAt(0), new SkipCommand(parseDouble(elements[3])));
            case "scale" -> this.registeredCommands.put(elements[1].charAt(0), new ScaleCommand(parseDouble(elements[3])));
            case "rotate" -> this.registeredCommands.put(elements[1].charAt(0), new RotateCommand(parseDouble(elements[3])));
            case "color" -> this.registeredCommands.put(elements[1].charAt(0), new ColorCommand(Color.decode("0x"+ elements[3])));
            case "push" -> this.registeredCommands.put(elements[1].charAt(0), new PushCommand());
            case "pop" -> this.registeredCommands.put(elements[1].charAt(0), new PopCommand());
            default -> throw new IllegalArgumentException("Unsupported command \"" + elements[2] + "\"!");
        }
    }

    /**
     * Creates an array with all necessary information for storing a command or a production.
     *
     * @param symbol character tied to the command or production.
     * @param info information for the command or production.
     * @param type string representing whether a command or a production is being processed.
     *
     * @return array representing the right-hand side of a command or a production.
     */
    private String[] createRHSArray(char symbol, String info, String type) {
        if (!type.equals("production") && !type.equals("command")) throw new IllegalArgumentException("Invalid type given, expected production or command, got \"" + type +
                "\"!");
        String[] infoArray = Objects.requireNonNull(info, "The given production cannot be null!").trim().split("\\s+");
        String[] RHSArray = new String[infoArray.length + 2];

        RHSArray[0] = type;
        RHSArray[1] = String.valueOf(symbol);
        for (int i = 2, length = infoArray.length + 2; i < length; i++) {
            RHSArray[i] = infoArray[i-2];
        }

        return RHSArray;
    }

    /**
     * The {@code LSystemImpl} class is an implementation of {@link LSystem} responsible for generating a string for the given level of the LSystem and drawing it.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private class LSystemImpl implements LSystem {

        /**
         * Generates the string of characters to be drawn after applying a certain amount of productions, determined by the given {@code level}.
         *
         * @param level level of the LSystem's string that is to be generated.
         * @return generated string.
         */
        @Override
        public String generate(int level) {
            String lastGeneratedString = axiom;

            for (int lvl = 0; lvl < level; lvl++) {
                StringBuilder sb = new StringBuilder();

                for (int i = 0, stringLength = lastGeneratedString.length(); i < stringLength; i++){
                    char c = lastGeneratedString.charAt(i);

                    String production = registeredProductions.get(c);
                    if (production == null) sb.append(c);
                    else sb.append(production);
                }

                lastGeneratedString = sb.toString();
            }
            return lastGeneratedString;
        }

        /**
         * Draws the LSystem.
         *
         * @param level level of the LSystem that is to be drawn.
         * @param painter {@link Painter} instance that draws the LSystem.
         * @throws IllegalArgumentException when the given {@code level} is negative.
         * @throws NullPointerException when the given {@code level} is {@code null}.
         */
        @Override
        public void draw(int level, Painter painter) {
            Objects.requireNonNull(painter, "The given painter cannot be null!");
            if (level < 0) throw new IllegalArgumentException("The given level cannot be negative!");

            Context ctx = new Context();

            ctx.pushState(new TurtleState(origin.copy(), new Vector2D(1, 0).rotated(toRadians(angle)), DEFAULT_COLOUR, unitLength * pow(unitLengthDegreeScaler, level)));

            String generatedString = this.generate(level);

            for (int i = 0, stringLength = generatedString.length(); i < stringLength; i++){
                char c = generatedString.charAt(i);

                Command fetchedCommand = registeredCommands.get(c);
                if (fetchedCommand == null) continue;

                fetchedCommand.execute(ctx, painter);
            }
        }
    }

}
