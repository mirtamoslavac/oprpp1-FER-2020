package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * The {@code CatShellCommand} class enables reading and displaying the content of a specific file on the console. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.1
 */
public class CatShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "cat";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Writes the content of the given file to the console.",
            "It expects either one or two arguments.",
            "If a single argument is given, that being a file name, then the content of the file will be written to the console using the default charset.",
            "If two arguments are given, those being a file name and a charset name, tthen the content of the file will be written to the console using the given charset.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = CommandUtils.checkPathWithSpaces(arguments.trim().split("\\s+"));

        if (argumentsArray.length > 2) {
            CommandUtils.oneOrTwoArguments(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }

        if(argumentsArray[0].equals("")) {
            CommandUtils.oneOrTwoArguments(env, COMMAND_NAME, 0);
            return ShellStatus.CONTINUE;
        }

        Path path = CommandUtils.getPath(env, argumentsArray[0]);
        if(path == null) return ShellStatus.CONTINUE;
        if(!CommandUtils.checkIfExists(env, path)) return ShellStatus.CONTINUE;

        if(!Files.isRegularFile(path)) {
            env.writeln("The given path (\"" + argumentsArray[0] + "\") is not a file!");
            return ShellStatus.CONTINUE;
        }

        Charset charset;
        if (argumentsArray.length == 1) charset = Charset.defaultCharset();
        else {
            try {
                charset = Charset.forName(argumentsArray[1]);
            } catch (UnsupportedCharsetException e) {
            env.writeln("The charset given is not supported!");
            return ShellStatus.CONTINUE;
            } catch (IllegalArgumentException e) {
            env.writeln("Invalid charset name given, got " + e.getMessage());
            return ShellStatus.CONTINUE;
            }
        }

        writeContentToConsole(env, path, charset);

        return ShellStatus.CONTINUE;
    }

    /**
     * Writes the content of the given file path {@code path} line by line to the console.
     *
     * @param env the environment the content is to be written to.
     * @param path path of the file that is to be read from.
     * @param charset  charset used to interpret chars from read bytes.
     */
    private void writeContentToConsole(Environment env, Path path, Charset charset) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(Files.newInputStream(path)), charset))){
            br.lines().forEach(env::writeln);
        } catch(IOException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public List<String> getCommandDescription() {
        return Collections.unmodifiableList(COMMAND_DESCRIPTION);
    }
}
