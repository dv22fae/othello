
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

	public static void main(String [] args) {

		// Get and validate input.
		String positionString = getAndValidatePosition(args);
		double timeLimitSeconds = getAndValidateTime(args);

		// Define and make position and algorithm.
		OthelloPosition position = new OthelloPosition(positionString);
		OthelloAlgorithm algorithm = new AlphaBeta(new Heuristics());

		// The time when the time limit has ended.
		long stopTimeNanos = stopTimeInNanos(timeLimitSeconds);

		// Running the iterative deepening search until the time limit.
		OthelloAction bestAction = iterativeDeepeningSearch(position, algorithm, stopTimeNanos);

		// If no depth was evaluated completely, time ran out.
		if (bestAction == null) {
			bestAction = ifNoMoveWasGotten(position);
		}

		// Print the position.
		bestAction.print();
	}

	/**
	 * Reads and validates position from the command line.
	 *
	 * It ensures that the user gave atleast two arguments and that the position string that is also
	 * gotten from the commandline has 65 characters.
	 *
	 * @param args, commandline arguments.
	 * @return hands back the validated string that represents the position.
	 */
	private static String getAndValidatePosition(String[] args) {
		if (args.length < 2) {
			System.err.println("Error! This is too few arguments.");
			System.exit(1);
		}

		String posString = args[0];

		if (posString.length() > 65) {
			printErrorAndSetExit("String is to long! Should be 65 and got " + posString.length(), 1);
		}
		if (posString.length() < 65) {
			printErrorAndSetExit("String is to short! Should be 65 and got " + posString.length(), 1);
		}

		return posString;
	}

	/**
	 * Gets and validates the time limit.
	 *
	 * @param args, command line arguments.
	 * @return the time limit that the user defined in seconds.
	 */
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

	/**
	 * Prints a error message and also sets the exit code to 1 that exits the program.
	 *
	 * @param errorText, text that says what went wrong.
	 * @param exitCode, sets the exit status.
	 */
	private static void printErrorAndSetExit(String errorText, int exitCode) {
		System.err.println(errorText);
		System.exit(exitCode);
	}

	/**
	 * If no move was evalueted because no depth finished before time limit.
	 *
	 * @param position, the current position.
	 * @return first legal move or pass.
	 */
	private static OthelloAction ifNoMoveWasGotten(OthelloPosition position) {
		var moves = position.getMoves();

		if (moves.isEmpty()) {
			return new OthelloAction("pass");
		} else {
			return moves.getFirst();
		}
	}

	/**
	 * Computes real stop time in nanoseconds.
	 *
	 * @param timeLimitSeconds, time limit in seconds.
	 * @return time where the time limit will be reached.
	 */
	private static long stopTimeInNanos(double timeLimitSeconds) {
		long stopTimeInNanos = System.nanoTime() + (long) (timeLimitSeconds * ONE_SECOND_OF_NANOS);
		return stopTimeInNanos;
	}

	/**
	 * Runs Iterative Deepening Search with alphaBeta the time limit is reached.
	 *
	 * @param position, position to evaluate.
	 * @param algorithm, the algoritm for heuristics to use.
	 * @param stopTimeNanos the time limit.
	 * @return the best action from deepest search that was finnished.
	 */
	private static OthelloAction iterativeDeepeningSearch(OthelloPosition position, OthelloAlgorithm algorithm,
														  long stopTimeNanos) {
		OthelloAction bestAction = null;
		int searchDepth = 1;

		while (System.nanoTime() < stopTimeNanos) {
			algorithm.setSearchDepth(searchDepth);
			algorithm.setStopTime(stopTimeNanos);

			try {
				OthelloAction possibleBestAction = algorithm.evaluate(position);

				if (System.nanoTime() < stopTimeNanos) {
					bestAction = possibleBestAction;
					searchDepth++;
				} else {
					break;
				}
			} catch (TimeIsUpExeption e) {
				break;
			}
		}
		System.err.println(searchDepth);
		return bestAction;
	}
}