import java.awt.desktop.SystemEventListener;
import java.util.Timer;

public class Othello{
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
	 *
	 * Author: Henrik Björklund
	 */
	public static void main(String [] args){

        if (args.length < 2)
		{
            System.err.println("Error! This is too few arguments.");
            System.exit(1);
        }

		String posString = args[0];
		OthelloPosition pos;
		OthelloAlgorithm algorithm;
		OthelloAction move = null;

		if (posString.length() > 65) {
			System.err.println("Error! String is to long! Should be 65 and got " + posString.length() + ".");
			System.exit(1);
		}
		if (posString.length() < 65) {
			System.err.println("Error! String is to short! Should be 65 and got " + posString.length() + ".");
			System.exit(1);
		}

		Double timeLimitSeconds;
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

		// Iterative depth search with time limit.
		int searchDepth = 1;

		//if(args.length > 0){
			//posString = args[0];
		//}else{
			//posString = "WEEEEEEEEEEEEEEEEEEEEEEEEEEEOXEEEEEEXOEEEEEEEEEEEEEEEEEEEEEEEEEEE";
		//}
		//System.out.println(posString);
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
			move = algorithm.evaluate(pos);
			searchDepth++;
		}

		move.print();
	}
}