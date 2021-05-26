package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.util.*;

/**
 * The {@code MyShell} class represents a simple shell with a number of commands. It implements {@link Environment}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class MyShell implements Environment {
    /**
     * The symbol signaling the beginning of a prompt for the current shell environment.
     */
    private Character promptSymbol;
    /**
     * The symbol announcing multiple lines for the user's input within the current shell environment.
     */
    private Character moreLinesSymbol;
    /**
     * The symbol signaling that multiple lines are being taken into account for the user's input within the current shell environment.
     */
    private Character multilineSymbol;

    /**
     * Collection of all available commands within the environment.
     */
    private final SortedMap<String, ShellCommand> commands;
    /**
     * Status of the shell.
     */
    private ShellStatus status;
    /**
     * Scanner for reading user input.
     */
    private final Scanner sc;

    /**
     * Creates and initializes a new environment with corresponding signaling symbols and commands.
     *
     * @param sc scanner for reading user input from the environment's console.
     */
    public MyShell (Scanner sc) {
        this.promptSymbol = '>';
        this.moreLinesSymbol = '\\';
        this.multilineSymbol = '|';

        this.commands =  new TreeMap<>(Map.of("cat", new CatShellCommand(), "charsets", new CharsetsShellCommand(), "copy", new CopyShellCommand(),
                "exit", new ExitShellCommand(), "help", new HelpShellCommand(), "hexdump", new HexdumpShellCommand(), "ls", new LsShellCommand(),
                "mkdir", new MkdirShellCommand(), "symbol", new SymbolShellCommand(), "tree", new TreeShellCommand()));
        this.status = ShellStatus.CONTINUE;
        this.sc = sc;
    }

    /**
     * Program used to execute the shell environment.
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)){
            MyShell myShell = new MyShell(sc);

            myShell.writeln("Welcome to MyShell v 1.0");

            do {
                try {
                    myShell.write(myShell.getPromptSymbol().toString() + " ");
                    String[] userInputSeparated = myShell.readLine().trim().split("\\s+", 2);
                    String commandName = userInputSeparated[0].toLowerCase();
                    String arguments = userInputSeparated.length == 1 ? "" : userInputSeparated[1];
                    ShellCommand command = myShell.commands.get(commandName);
                    if (command == null) {
                        if (!commandName.isBlank()) myShell.writeln("Unknown command \"" + commandName + "\"!");
                        continue;
                    }
                    myShell.status = command.executeCommand(myShell, arguments);
                } catch (ShellIOException e) {
                    return;
                } catch (RuntimeException e) {
                    myShell.writeln(e.getMessage());
                }
            } while (!myShell.status.equals(ShellStatus.TERMINATE));
        }
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            StringBuilder sb = new StringBuilder();

            while(true) {
                String line = sc.nextLine().trim();
                if(!line.endsWith(getMorelinesSymbol().toString())) return sb.append(line).toString();

                sb.append(line, 0, line.length() - 1);
                write(getMultilineSymbol().toString() + " ");
            }

        } catch (RuntimeException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        try {
            System.out.print(Objects.requireNonNull(text));
        } catch (RuntimeException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        try {
            write(Objects.requireNonNull(text) + '\n');
        } catch (RuntimeException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public Character getMultilineSymbol() {
        return multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        this.multilineSymbol = Objects.requireNonNull(symbol, "The given symbol cannot be null!");
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.promptSymbol = Objects.requireNonNull(symbol, "The given symbol cannot be null!");
    }

    @Override
    public Character getMorelinesSymbol() {
        return moreLinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.moreLinesSymbol = Objects.requireNonNull(symbol, "The given symbol cannot be null!");
    }
}
