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

		String posString = args[0];
		OthelloPosition pos;
		OthelloAlgorithm algorithm;
		OthelloAction move = null;
		Double timeLimitSeconds;

        if (args.length < 2) {
            System.err.println("Error! This is too few arguments.");
            System.exit(1);
        }

		if (posString.length() > 65) {
			System.err.println("Error! String is to long! Should be 65 and got " + posString.length() + ".");
			System.exit(1);
		}
		if (posString.length() < 65) {
			System.err.println("Error! String is to short! Should be 65 and got " + posString.length() + ".");
			System.exit(1);
		}

		try {
			timeLimitSeconds = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("Timelimit must be a number in seconds!");
			System.exit(1);
			return;
		}

		if (timeLimitSeconds <= 0) {
			System.err.println("Time limit must be positive number!");
			System.exit(1);
		}

		long timeLimitNanos = (long) (timeLimitSeconds * 1_000_000_000L);
		long startTime = System.nanoTime();
		long stopTIme = startTime + timeLimitNanos;

		// Iterative depth search with time limit.
		int searchDepth = 1;

		OthelloAction bestAction = null;

		pos = new OthelloPosition(posString);
		//pos.illustrate(); //Only for debugging. The test script has it's own print method

		// Which evaluator (heuristics) should be used
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

			//move = algorithm.evaluate(pos);
			//searchDepth++;
		}

		// om inget djup hann evalueras.
		if (bestAction == null) {
			var moves = pos.getMoves();
			if (moves.isEmpty()) {
				new OthelloAction("pass");
			}
			else {
				moves.getFirst();
			}
		}

		//move.print();
		bestAction.print();
	}
}