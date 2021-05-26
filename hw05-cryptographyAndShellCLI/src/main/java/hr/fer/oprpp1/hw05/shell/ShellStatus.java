package hr.fer.oprpp1.hw05.shell;

/**
 * The {@code ShellStatus} enumeration represents possible responses to finishing some actions within the environment.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public enum ShellStatus {
    /**
     * Status used for letting the currently running environment run after some action is performed.
     */
    CONTINUE,

    /**
     * Status used for terminating the currently running environment.
     */
    TERMINATE

}
