package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * The {@code CopyShellCommand} class enables copying content from one file to another. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CopyShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "copy";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Copies the content from the given source file to the given destination.",
            "It expects two arguments, the source file and the destination file or destination folder paths.",
            "If the destination file already exists, the user has to declare whether to overwrite the existing content or to drop the action.",
            "If the destination is a directory, the source file will be copied to the file of the same name within the given directory.");
    /**
     * Size of the buffer for the input/output stream.
     */
    private static final int BUFFER_SIZE = 1024;

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = arguments.trim().split("\\s+");

        if(argumentsArray[0].equals("")) {
            CommandUtils.twoArguments(env, COMMAND_NAME, 0);
            return ShellStatus.CONTINUE;
        }

        if (argumentsArray.length != 2) {
            CommandUtils.twoArguments(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }

        Path sourcePath = CommandUtils.getPath(env, argumentsArray[0]);
        if(sourcePath == null) return ShellStatus.CONTINUE;
        if(!CommandUtils.checkIfExists(env, sourcePath)) return ShellStatus.CONTINUE;
        if(!CommandUtils.checkIfRegularFile(env, sourcePath)) return ShellStatus.CONTINUE;

        Path destinationPath = CommandUtils.getPath(env, argumentsArray[1]);
        if(Files.isDirectory(destinationPath)) {
            destinationPath = CommandUtils.getPath(env, argumentsArray[1] + "/" + sourcePath.getFileName().toString());
            if(destinationPath == null) return ShellStatus.CONTINUE;
        }
        if(!Files.notExists(destinationPath)) {
            if(!CommandUtils.checkIfRegularFile(env, destinationPath)) return ShellStatus.CONTINUE;
            int invalidResponseCounter = 0;
            while(invalidResponseCounter < 3) {
                env.write("Overwrite already existing file with the path (\"" + destinationPath.getFileName().toString() + "\")? (Yes/No) ");
                String response = env.readLine().trim();
                switch (response.toLowerCase()) {
                    case "y", "yes" -> {
                        copyToFile(env, sourcePath, destinationPath);
                        return ShellStatus.CONTINUE;
                    }
                    case "n", "no" -> {
                        return ShellStatus.CONTINUE;
                    }
                    default -> env.writeln("(" + ++invalidResponseCounter + "/3) Invalid response! Expected y(es)/n(o), got \"" + response + "\"");
                }
            }
        } else copyToFile(env, sourcePath, destinationPath);

        return ShellStatus.CONTINUE;
    }

    /**
     * Copies the content from one file to another using buffered I/O streams.
     *
     * @param env the environment in which the command was called.
     * @param sourcePath the file from which to copy the content.
     * @param destinationPath the file to which to copy the content.
     */
    private void copyToFile(Environment env, Path sourcePath, Path destinationPath) {
        try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(sourcePath));
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(destinationPath))) {

            byte[] buf = new byte[BUFFER_SIZE];
            do {
                int k = bis.read(buf);
                if (k == -1) break;

                bos.write(buf, 0, k);
            } while (true);
        } catch (IOException e) {
            env.writeln("An exception occurred while trying to copy from \"" + sourcePath.toString() + "\" to \"" + destinationPath.toString() + "\"!");
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
