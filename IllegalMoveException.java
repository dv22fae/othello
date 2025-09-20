/**
 * This exception is supposed to be thrown when an OthelloPosition is asked to
 * make a move that is not legal in the position.
 *
 * @author Henrik Björklund, original
 * @author Fredrik Alexandre, co-author
 * @author Samuel Hagner, co-author
 * @version 2.0, 2025-09-24
 */

public class IllegalMoveException extends Exception {
	private OthelloAction action;

	/**
	 * Makes a exception for the given illegal action.
	 *
	 * @param a, action that was not legal.
	 */
	public IllegalMoveException(OthelloAction a) {
		action = a;
	}

	/**
	 * Returns action that caused this exception.
	 *
	 * @return illegal action.
	 */
	public OthelloAction getAction() {
		return action;
	}
}