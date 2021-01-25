package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * The {@code HexdumpShellCommand} class enables representing the hex-output of a given file.
 *
 * @author mirtamoslavac
 * @version 1.1
 */
public class HexdumpShellCommand implements ShellCommand {
    /**
     * The name of the specific command.
     */
    private static final String COMMAND_NAME = "hexdump";
    /**
     * The description of the specific command.
     */
    private static final List<String> COMMAND_DESCRIPTION = List.of("Creates a hex-output of the given file, as well as the characters within that file.",
            "It expects a single argument, the path of the file to be processed.",
            "Showcases only a standard subset of characters as a part of the output.",
            "All other characters are replaced with a '.'.");
    /**
     * Maximum number of bytes in hexadecimal notation within one row.
     */
    private static final int OFFSET = 16;

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        CommandUtils.checkArguments(env, arguments);
        String[] argumentsArray = CommandUtils.checkPathWithSpaces(arguments.trim().split("\\s+"));

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

        if(!Files.isRegularFile(path)) {
            env.writeln("The given path (\"" + argumentsArray[0] + "\") is not a file!");
            return ShellStatus.CONTINUE;
        }

        try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path))) {
            int rowNumber = -1;
            byte[] buf = bis.readNBytes(OFFSET);
            while(buf.length != 0) {
                String formattedRow = formatRow(buf, ++rowNumber);
                env.writeln(formattedRow);
                buf = bis.readNBytes(OFFSET);
            }
        } catch (IOException e) {
            env.writeln("An exception occurred while reading from the given file with the path (\"" + path.toString() + "\")!");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Formats the given {@code buf} byte array as a hex-output row.
     *
     * @param buf byte array containing bytes to be processed as a hex-output row.
     * @param rowNumber used to calculate how many bytes have been processed up to that row.
     * @return formatted row to be written to the console.
     */
    private String formatRow(byte[] buf, int rowNumber) {
        StringBuilder sbMain = new StringBuilder();
        StringBuilder sbHex1 = new StringBuilder();
        StringBuilder sbHex2 = new StringBuilder();
        StringBuilder sbASCII = new StringBuilder();

        sbMain.append(String.format("%08X:", rowNumber * OFFSET));

        for (int i = 0, length = buf.length; i < length; i++) {
            if(i < OFFSET / 2) sbHex1.append(String.format(" %02X", buf[i]));
            else sbHex2.append(String.format("%02X ", buf[i]));
            sbASCII.append(String.format("%c", (buf[i] < 32) ? '.' : buf[i]));
        }

        int spaceIndicator = 3 * OFFSET / 2;

        return sbMain.append(String.format("%-" + (OFFSET % 2 == 0 ? spaceIndicator : (spaceIndicator - 2))+ "s|", sbHex1.toString()))
                .append(String.format("%-" + (OFFSET % 2 == 0 ? spaceIndicator : 3 * (OFFSET - OFFSET / 2)) + "s| ", sbHex2.toString()))
                .append(sbASCII.toString()).toString();
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
