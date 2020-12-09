package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The {@code CommandUtils} class contains methods used by multiple commands that implement {@link hr.fer.oprpp1.hw05.shell.ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CommandUtils {
    /**
     * Checks whether the given Environment instance {@code env} and {@code arguments} are {@code null}.
     *
     * @param env the environment the command is to be executed in.
     * @param arguments given arguments of the command to be checked.
     * @throws NullPointerException when @code env} or {@code arguments} are {@code null}.
     */
    public static void checkArguments(Environment env, String arguments) {
        Objects.requireNonNull(env, "The given environment cannot be null!");
        Objects.requireNonNull(arguments, "The given string of arguments cannot be null!");
    }

    /**
     * Writes an error message to the console about receiving arguments, when none are required.
     *
     * @param env the environment the command was attempted to be executed in.
     * @param commandName the name of the command that was attempted to be executed.
     * @param argumentNumber number of arguments received through user input.
     */
    public static void noArguments(Environment env, String commandName, int argumentNumber) {
        env.writeln("The " + commandName + " command cannot take any arguments, got " + argumentNumber + "!");
    }

    /**
     * Writes an error message to the console about receiving more than one argument, when none or a single one was expected.
     *
     * @param env the environment the command was attempted to be executed in.
     * @param commandName the name of the command that was attempted to be executed.
     * @param argumentNumber number of arguments received through user input.
     */
    public static void noneOrSingleArgument(Environment env, String commandName, int argumentNumber) {
        env.writeln("The " + commandName + " command either expects no arguments or a single one, got " + argumentNumber + "!");
    }

    /**
     * Writes an error message to the console about not receiving exactly one argument.
     *
     * @param env the environment the command was attempted to be executed in.
     * @param commandName the name of the command that was attempted to be executed.
     * @param argumentNumber number of arguments received through user input.
     */
    public static void singleArgument(Environment env, String commandName, int argumentNumber) {
        env.writeln("The " + commandName + " command expects only one argument, got " + argumentNumber + "!");
    }

    /**
     * Writes an error message to the console about receiving none or more than two arguments, when one or two were expected.
     *
     * @param env the environment the command was attempted to be executed in.
     * @param commandName the name of the command that was attempted to be executed.
     * @param argumentNumber number of arguments received through user input.
     */
    public static void oneOrTwoArguments(Environment env, String commandName, int argumentNumber) {
        env.writeln("The " + commandName + " command expects either one or two arguments, got " + argumentNumber + "!");
    }

    /**
     * Writes an error message to the console about not receiving exactly two arguments.
     *
     * @param env the environment the command was attempted to be executed in.
     * @param commandName the name of the command that was attempted to be executed.
     * @param argumentNumber number of arguments received through user input.
     */
    public static void twoArguments(Environment env, String commandName, int argumentNumber) {
        env.writeln("The " + commandName + " command expects two arguments, got " + argumentNumber + "!");
    }

    /**
     * Tries to get the path of a file or a directory from its textual representation {@code pathString}.
     *
     * @param env the environment the command is to be executed in.
     * @param pathString textual representation of the path.
     * @throws InvalidPathException when the given path is invalid.
     * @return the path of the given textual representation.
     */
    public static Path getPath(Environment env, String pathString) {
        Path path = null;
        try {
            path = Paths.get(pathString);
        } catch (InvalidPathException e) {
            env.writeln("Invalid path given! Got \"" + pathString + "\"");
        }

        return path;
    }

    /**
     * Checks whether the file or directory characterized by the given {@code path} exists.
     *
     * @param env the environment the command is to be executed in.
     * @param path path that is to be checked.
     * @return {@code true} if it exists, {@code false} otherwise.
     */
    public static boolean checkIfExists(Environment env, Path path) {
        if(Files.notExists(path)) {
            env.writeln("The given path (\"" + path.toString() + "\") does not exist!");
            return false;
        }
        return true;
    }

    /**
     * Checks whether the given {@code path} is a directory.
     *
     * @param env the environment the command is to be executed in.
     * @param path path that is to be checked.
     * @return {@code true} if it is a directory, {@code false} otherwise.
     */
    public static boolean checkIfDirectory(Environment env, Path path) {
        if(!Files.isDirectory(path)) {
            env.writeln("The given path (\"" + path.toString() + "\") is not a directory!");
            return false;
        }
        return true;
    }

    /**
     * Checks whether the given {@code path} is a regular file.
     *
     * @param env the environment the command is to be executed in.
     * @param path path that is to be checked.
     * @return {@code true} if it is a file, {@code false} otherwise.
     */
    public static boolean checkIfRegularFile(Environment env, Path path) {
        if(!Files.isRegularFile(path)) {
            env.writeln("The given path (\"" + path.toString() + "\") is not a file!");
            return false;
        }
        return true;
    }

}
