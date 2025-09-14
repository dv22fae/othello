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

		String posString;
		OthelloPosition pos;
		OthelloAlgorithm algorithm;
		OthelloAction move = null;

		if(args.length > 0){
			posString = args[0];
		}else{
			posString = "WEEEEEEEEEEEEEEEEEEEEEEEEEEEOXEEEEEEXOEEEEEEEEEEEEEEEEEEEEEEEEEEE";
		}
		//System.out.println(posString);
		pos = new OthelloPosition(posString);
		//pos.illustrate(); //Only for debugging. The test script has it's own print method

		// Which evaluator (heuristics) should be used
		algorithm = new AlphaBeta(new EarlyGame());

		// Iterative depth search with time limit.
		int searchDepth = 1;
		long timeLimit = Long.parseLong(args[1]) * 1_000_000;
		long startTime = System.nanoTime();
		while(true){
			long timeTaken = System.nanoTime() - startTime;
			if(timeTaken >= timeLimit){
				break;
			}
			algorithm.setSearchDepth(searchDepth);
			move = algorithm.evaluate(pos);

			searchDepth++;
		}

		move.print();
	}
}