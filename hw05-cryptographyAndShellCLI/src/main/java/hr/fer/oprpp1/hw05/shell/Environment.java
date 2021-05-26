package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * The {@code Environment} interface that represents a model for building an environment to execute certain commands in.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public interface Environment {

    /**
     * Reads a line that was generated through user input from the current environment.
     *
     * @throws ShellIOException when some exception occurs while attempting to read from the current environment.
     * @return text that was read from the current environment.
     */
    String readLine() throws ShellIOException;

    /**
     * Writes a piece of text to the environment.
     *
     * @param text a piece of text that is to be written to the current environment.
     * @throws ShellIOException when some exception occurs while attempting to write to the current environment.
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes an entire row that is to the environment.
     *
     * @param text a row that is to be written to the current environment.
     * @throws ShellIOException when some exception occurs while attempting to write to the current environment.
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Fetches all commands available within the current environment.
     *
     * @return unmodifiable map of commands.
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Fetches the current multiline symbol within the current environment.
     *
     * @return current multiline symbol.
     */
    Character getMultilineSymbol();

    /**
     * Changes the current multiline symbol within the current environment to the given {@code symbol}.
     *
     * @param symbol new multiline symbol.
     * @throws NullPointerException when the given {@code symbol} is {@code null}.
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Fetches the current prompt symbol within the current environment.
     *
     * @return current prompt symbol.
     */
    Character getPromptSymbol();

    /**
     * Changes the current prompt symbol within the current environment to the given {@code symbol}.
     *
     * @param symbol new prompt symbol.
     * @throws NullPointerException when the given {@code symbol} is {@code null}.
     */
    void setPromptSymbol(Character symbol);

    /**
     * Fetches the current morelines symbol within the current environment.
     *
     * @return current morelines symbol.
     */
    Character getMorelinesSymbol();

    /**
     * Changes the current morelines symbol within the current environment to the given {@code symbol}.
     *
     * @param symbol new morelines symbol.
     * @throws NullPointerException when the given {@code symbol} is {@code null}.
     */
    void setMorelinesSymbol(Character symbol);

}
