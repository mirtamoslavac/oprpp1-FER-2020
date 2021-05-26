package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * The {@code CharsetsShellCommand} class represents the command for listing all supported charsets within the used Java platform. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CharsetsShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "charsets";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Lists all supported charsets within the used Java platform.", "It takes no arguments.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);

        if (!arguments.isBlank()) {
            CommandUtils.noArguments(env, COMMAND_NAME, arguments.split("\\s+").length);
            return ShellStatus.CONTINUE;
        }

        Charset.availableCharsets().keySet().forEach(env::writeln);

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
