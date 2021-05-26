package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.List;

/**
 * The {@code HelpShellCommand} class offers information about a certain command available within the environment or lists all commands available within the environment. It
 * implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class HelpShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "help";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Offers information about existing commands available within the environment.",
            "It expects either no arguments or a single argument.",
            "If no argument is given, then the names of all commands available within the environment will be listed.",
            "If a single argument is given, that being a certain existing command, then the description of that relevant command will be shown.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = arguments.trim().split("\\s+");

        if (argumentsArray.length > 1) {
            CommandUtils.noneOrSingleArgument(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }

        if (argumentsArray[0].isBlank()) env.commands().keySet().forEach(env::writeln);
        else {
            if(!env.commands().containsKey(argumentsArray[0])) {
                env.writeln("Unknown command \"" + argumentsArray[0] + "\"!");
                return ShellStatus.CONTINUE;
            }

            boolean first = true;
            for (String descriptionSentence : env.commands().get(argumentsArray[0]).getCommandDescription()) {
                if(first) {
                    env.writeln(argumentsArray[0] + String.format("%s%s", " ".repeat(12 - argumentsArray[0].length()), descriptionSentence));
                    first = false;
                }
                else env.writeln(String.format("%s%s", " ".repeat(12), descriptionSentence));
            }
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
