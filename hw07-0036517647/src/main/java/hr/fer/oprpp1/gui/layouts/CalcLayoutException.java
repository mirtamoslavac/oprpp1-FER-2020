package hr.fer.oprpp1.gui.layouts;

/**
 * Thrown to indicate that an illegal grid position was given.
 * 
 * @author mirtamoslavac
 * @version 1.0
 */
public class CalcLayoutException extends RuntimeException {
	@java.io.Serial
	private static final long serialVersionUID = 951753826404568529L;

	/**
	 * Constructs an {@code CalcLayoutException} with no detail message.
	 */
	public CalcLayoutException() {
	}

	/**
	 * Constructs an {@code CalcLayoutException} with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
	
}
