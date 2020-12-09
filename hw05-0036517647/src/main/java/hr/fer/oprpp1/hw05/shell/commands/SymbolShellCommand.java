package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.List;

/**
 * The {@code SymbolShellCommand} class offers information about special signaling symbols within the environment or enables setting new values to those same symbols. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class SymbolShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "symbol";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Offers information about special signaling symbols within the environment.",
            "It expects either one or two arguments.",
            "If a single argument is given, that being a name of an existing symbol, then the value of that relevant symbol will be shown.",
            "If two arguments are given, those being a name of an existing symbol and a new value, then the value of that relevant symbol will be set to the given one.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = arguments.trim().split("\\s+");

        if (argumentsArray.length > 2) {
            CommandUtils.oneOrTwoArguments(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }

        if(argumentsArray.length == 1 && argumentsArray[0].equals("")) {
            CommandUtils.oneOrTwoArguments(env, COMMAND_NAME, 0);
            return ShellStatus.CONTINUE;
        }

        switch (argumentsArray[0].toLowerCase()) {
            case "prompt", "morelines", "multiline" -> {
                 if (argumentsArray.length == 2) {
                    char[] symbol = argumentsArray[1].toCharArray();
                    if (symbol.length != 1) {
                        env.writeln("Invalid special symbol value! Expected a single character, got " + argumentsArray[1] + "!");
                        return ShellStatus.CONTINUE;
                    }
                    changeSymbol(env, argumentsArray[0], symbol[0]);
                } else writeSymbol(env, argumentsArray[0]);
            }
            default -> env.writeln("Invalid special symbol type! Expected PROMPT, MORELINES or MULTILINE, got \"" + argumentsArray[0].toUpperCase() + "\"!");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Sets the new value of the symbol with the {@code symbolName} to the given {@code newValue}.
     *
     * @param env the environment the command is being executed in.
     * @param symbolName name of the symbol within the environment.
     * @param newValue value that is to be set.
     */
    private void changeSymbol(Environment env, String symbolName, Character newValue) {
        Character oldValue = null;

        switch (symbolName.toLowerCase()) {
            case "prompt" -> {
                oldValue = env.getPromptSymbol();
                env.setPromptSymbol(newValue);
            }
            case "morelines" -> {
                oldValue = env.getMorelinesSymbol();
                env.setMorelinesSymbol(newValue);
            }
            case "multiline" -> {
                oldValue = env.getMultilineSymbol();
                env.setMultilineSymbol(newValue);
            }
        }

        env.writeln("Symbol for " + symbolName.toUpperCase() + " changed from '" + oldValue + "' to '" + newValue + "'");
    }

    /**
     * Writes the current value of the symbol with the {@code symbolName} to the console of the environment {@code env}.
     *
     * @param env the environment the command is being executed in.
     * @param symbolName name of the symbol within the environment.
     */
    private void writeSymbol(Environment env, String symbolName) {
        Character value = null;

        switch (symbolName.toLowerCase()) {
            case "prompt" -> value = env.getPromptSymbol();
            case "morelines" -> value = env.getMorelinesSymbol();
            case "multiline" -> value = env.getMultilineSymbol();
        }

        env.writeln("Symbol for " + symbolName.toUpperCase() + " is '" + value + "'");
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
