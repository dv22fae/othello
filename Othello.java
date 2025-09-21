import java.awt.desktop.SystemEventListener;
import java.util.Timer;

/**
 * Main entry point for the Othello game search.
 *
 * Current behavior:
 *   - Uses Alpha-Beta pruning with a fixed search depth.
 *   - Prints the best move found at that depth.
 *
 * Assignment requirement:
 *   - Replace fixed-depth search with Iterative Deepening Search (IDS)
 *     that respects a time limit (provided as an argument).
 *
 * Usage:
 *   java Othello <position_string> <time_limit_seconds>
 *
 * Args:
 *   arg0: Position string (length 65, board representation).
 *   arg1: Time limit in seconds for IDS (float or int).
 *
 * @author Henrik Björklund, original
 * @author Fredrik Alexandre, co-author
 * @author Samuel Hagner, co-author
 * @version 2.0, 2025-09-24
 */

public class Othello {

	private static final int LENGTH_OF_POSITION = 65;
	private static final long ONE_SECOND_OF_NANOS = 1_000_000_000L;

	/**
	 * Main method
	 *
	 * It validates the input arguments, builds position, and runs
	 * alphaBeta search with iterative deepening up to the time limit.
	 * When time is up a TimeIsUpExeption is thrown.
	 *
	 * @param args args[0] is the 65 character position string,
	 * 						args[1] is the time limit in seconds.
	 */
	public static void main(String [] args) {

		// Get and validate input.
		String posString = getAndValidatePosition(args);
		double timeLimitSeconds = getAndValidateTime(args);

		OthelloPosition pos;
		OthelloAlgorithm algorithm;
		OthelloAction move = null;

		long timeLimitNanos = (long) (timeLimitSeconds * ONE_SECOND_OF_NANOS);
		long startTime = System.nanoTime();
		long stopTIme = startTime + timeLimitNanos;

		// Iterative depth search with time limit.
		int searchDepth = 1;

		OthelloAction bestAction = null;

		pos = new OthelloPosition(posString);

		// Which evaluator, heuristics, should be used.
		algorithm = new AlphaBeta(new EarlyGame());

		while (true) {
			long timeTaken = System.nanoTime() - startTime;

			if (timeTaken >= timeLimitNanos) {
				break;
			}

			algorithm.setSearchDepth(searchDepth);
			algorithm.setStopTime(stopTIme);

			try {
				OthelloAction possibleBestAction = algorithm.evaluate(pos);

				if (System.nanoTime() < stopTIme) {
					bestAction = possibleBestAction;
					searchDepth++;
				} else {
					break;
				}
			} catch (TimeIsUpExeption e) {
				break;
			}
		}

		// If no depth was evaluated completely, time ran out.
		if (bestAction == null) {
			bestAction = ifNoMoveWasGotten(pos);
		}

		bestAction.print();
	}

	private static String getAndValidatePosition(String[] args) {
		if (args.length < 2) {
			System.err.println("Error! This is too few arguments.");
			System.exit(1);
		}

		String posString = args[0];

		if (posString.length() > 65) {
			printErrorAndSetExit("String is to long! Should be 65 and got " + posString.length() + ".", 1);
		}
		if (posString.length() < 65) {
			printErrorAndSetExit("String is to short! Should be 65 and got " + posString.length() + ".", 1);
		}

		return posString;
	}

	private static double getAndValidateTime(String[] args) {
		double timeLimitSeconds;
		try {
			timeLimitSeconds = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			printErrorAndSetExit("Timelimit must be a number in seconds!", 1);
			return 0;
		}

		if (timeLimitSeconds <= 0) {
			printErrorAndSetExit("Time limit must be positive number!", 1);
		}
		return timeLimitSeconds;
	}

	private static void printErrorAndSetExit(String errorText, int exitCode) {
		System.err.println(errorText);
		System.exit(exitCode);
	}

	private static OthelloAction ifNoMoveWasGotten(OthelloPosition pos) {
		var moves = pos.getMoves();

		if (moves.isEmpty()) {
			return new OthelloAction("pass");
		} else {
			return moves.getFirst();
		}
	}
}