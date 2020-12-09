package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The {@code TreeShellCommand} class enables recursively writing the directory structure of the given directory to the console. It implements {@link ShellCommand}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class TreeShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "tree";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Creates a graphical representation of the directory structure recursively.", 
							"It expects a single argument, a directory whose tree will be printed on the console.");
	
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

        try {
            Files.walkFileTree(path, new TreeSimpleFileVisitor(env));
        } catch (IOException e) {
            env.writeln("An I/O exception occurred while going through the given directory with the path \"" + argumentsArray[0] + "\"!");
        } catch (SecurityException e) {
            env.writeln("A security violation within the given directory with the path (\"" + argumentsArray[0] + "\") occurred!");
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

	/**
	* The {@code TreeSimpleFileVisitor} class enables visiting each file within the given directory and performing actions accordingly. It extends {@link SimpleFileVisitor}.
	*
	* @author mirtamoslavac
	* @version 1.0
	*/
    private static class TreeSimpleFileVisitor extends SimpleFileVisitor<Path> {

		/**
		* The environment the command is being executed in.
		*/
        private final Environment env;
		/**
		* The depth of the tree that is being visited.
		*/
        private int depth;
		/**
		* The number of spaces in the graphical representation of the tree used to distinguish two different depths of the tree.
		*/
        private static final int INDENTATION = 2;

        public TreeSimpleFileVisitor(Environment env) {
            this.env = env;
            this.depth = 0;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            Objects.requireNonNull(attrs);

            printRow(env, Objects.requireNonNull(dir));
            depth++;

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            Objects.requireNonNull(attrs);

            printRow(env, Objects.requireNonNull(file));

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Objects.requireNonNull(dir);

            depth--;

            if (exc != null) throw exc;
            return FileVisitResult.CONTINUE;
        }

		/**
		* Writes the file or directory within the tree and places it within the context of the file tree on the console of the environment {@code env}.
		*
		* @param env the environment the formatted row is to be printed on.
		* @param file path of the file or directory that is to be printed on the console.
		*/
        private void printRow(Environment env, Path file) {
            env.writeln(String.format("%s%s", " ".repeat(depth * INDENTATION), file.getFileName().toString()));
        }
    }
}
