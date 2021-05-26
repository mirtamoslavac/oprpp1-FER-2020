package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.List;

/**
 * The {@code ExitShellCommand} class represents the command for terminating the execution of the currently running environment. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class ExitShellCommand implements ShellCommand {

    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "exit";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Terminates the currently running environment.", "It takes no arguments.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);

        if (!arguments.isBlank()) {
            CommandUtils.noArguments(env, COMMAND_NAME, arguments.split("\\s+").length);
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.TERMINATE;
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
