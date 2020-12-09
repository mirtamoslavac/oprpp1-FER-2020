package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code LsShellCommand} class enables writing the directory listing of the given directory to the console. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class LsShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "ls";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Writes the directory listing of the given directory to the console.",
            "It expects a single argument, the path of the directory.");
    /**
     * {@link SimpleDateFormat} instance for formatting the date and time.
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = arguments.trim().split("\\s+");

        if (argumentsArray.length != 1) {
            CommandUtils.singleArgument(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }

        if(argumentsArray[0].equals("")) {
            CommandUtils.singleArgument(env, COMMAND_NAME, 0);
            return ShellStatus.CONTINUE;
        }

        Path path = CommandUtils.getPath(env, argumentsArray[0]);
        if(path == null) return ShellStatus.CONTINUE;
        if(!CommandUtils.checkIfExists(env, path)) return ShellStatus.CONTINUE;

        if(!Files.isDirectory(path)) {
            env.writeln("The given path (\"" + argumentsArray[0] + "\") is not a directory!");
            return ShellStatus.CONTINUE;
        }

        try (Stream<Path> stream = Files.list(path)){
            for (Path file : stream.collect(Collectors.toList())){
                String row = createOutputRow(env, file);
                if(row == null) return ShellStatus.CONTINUE;
                env.writeln(row);
            }
        } catch (IOException e) {
            env.writeln("Cannot get directory listing of (\"" + argumentsArray[0] + "\")!");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Creates an output row for a single file or directory within the directory listing.
     *
     * @param env the environment the command is to be executed in.
     * @param path file or directory that is to be processed.
     * @return a formatted output row.
     */
    private String createOutputRow(Environment env, Path path) {
        BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        try {
            BasicFileAttributes attributes = faView.readAttributes();
            return formatOutputRow(path, attributes);
        } catch (IOException e) {
            env.writeln("Cannot read properties for the file with the path (\"" + path.toString() + "\")!");
            return null;
        }
    }

    /**
     * Formats an output row with information from the given path and its attributes.
     *
     * @param path file or directory that is to be processed.
     * @param attributes attributes of the given {@code path}.
     * @return formatted row.
     */
    private String formatOutputRow(Path path, BasicFileAttributes attributes) {
        FileTime fileTime = attributes.creationTime();
        String formattedDateTime = SDF.format(new Date(fileTime.toMillis()));

        return String.format("%s%s%s%s %10d %s %s",
                attributes.isDirectory() ? "d" : "-",
                Files.isReadable(path) ? "r" : "-",
                Files.isWritable(path) ? "w" : "-",
                Files.isExecutable(path) ? "x" : "-",
                attributes.size(),
                formattedDateTime,
                path.getFileName().toString());
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
