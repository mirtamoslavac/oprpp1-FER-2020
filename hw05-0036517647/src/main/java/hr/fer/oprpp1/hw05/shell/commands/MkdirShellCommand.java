package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * The {@code MkdirShellCommand} class enables creating a directory structure within the current working directory. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.1
 */
public class MkdirShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "mkdir";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Creates a directory structure within the current working directory.",
            "It expects a single argument, the path of the to-be-created directory structure.",
            "If some of the directories within the given path already exist, then it just creates the nonexistent ones within those that do.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = CommandUtils.checkPathWithSpaces(arguments.trim().split("\\s+"));

        if (argumentsArray.length != 1 || argumentsArray[0].isBlank())
            CommandUtils.singleArgument(env, COMMAND_NAME, argumentsArray.length == 1 ? 0 : argumentsArray.length);

        Path path = CommandUtils.getPath(env, argumentsArray[0]);
        if(path == null) return ShellStatus.CONTINUE;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            env.writeln("The given path (\"" + argumentsArray[0] + "\") cannot be created as a directory structure!");
        }

        return ShellStatus.CONTINUE;
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
