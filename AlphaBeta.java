import java.util.LinkedList;
import java.util.Timer;

/**
 * This is where you implement the alpha-beta algorithm.
 * See <code>OthelloAlgorithm</code> for details.
 *
 * Original version by Henrik Björklund, changed by co-authors.
 *
 * @author Henrik Björklund, original
 * @author Fredrik Alexandre, co-author
 * @author Samuel Hagner, co-author
 *
 * @version 2.0, 2025-09-24
 */
public class AlphaBeta implements OthelloAlgorithm {

	private static final int NEG_INFINITY = Integer.MIN_VALUE;
	private static final int POS_INFINITY = Integer.MAX_VALUE;

	protected int searchDepth;
	protected static final int DefaultDepth = 7;
	protected OthelloEvaluator evaluator;
	private long stop = Long.MAX_VALUE;

	/**
	 * Sets a stop time for search.
	 *
	 * When the time goes passed this value, the search will
	 * quite by throwing TimeIsUpExeption.
	 *
	 * @param stop, absolute deadline.
	 */
	public void setStopTime (long stop) {
		this.stop = stop;
	}

	/**
	 * Checks if the stop time has been reached.
	 *
	 * If the current time has passed the stop time,
	 * it throws TimeIsUpExeption and the seach will end.
	 *
	 * @throws TimeIsUpExeption, if stop time is reached.
	 */
	private void stopTimeOfNot() {
		if (System.nanoTime() >= stop) {
			throw new TimeIsUpExeption();
		}
	}

	/**
	 * Creates an alpha-beta searcher with the default evaluator
	 * and the default search depth.
	 */
	public AlphaBeta() {
		evaluator = new CountingEvaluator();
		searchDepth = DefaultDepth;
	}

	public AlphaBeta(OthelloEvaluator eval) {
		evaluator = eval;
		searchDepth = DefaultDepth;
	}

	public AlphaBeta(OthelloEvaluator eval, int depth) {
		evaluator = eval;
		searchDepth = depth;
	}

	public void setEvaluator(OthelloEvaluator eval) {
		evaluator = eval;
	}

	public void setSearchDepth(int depth) {
		searchDepth = depth;
	}


	public OthelloAction evaluate(OthelloPosition pos) {
		stopTimeOfNot();
		OthelloAction bestAction = null;

		// Keeps track on the best action from the root (current position).
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop at the bottom of the tree or if no possible move is available.
		if(possibleActions.isEmpty()){
			return new OthelloAction("pass");
		}

		// Initiera bästa kända score på roten utifrån vem som ska spela.
		int bestScore;
		if (pos.toMove()) {
			// börja så lågt som möjligt
			bestScore = NEG_INFINITY;
		} else {
			// börja så högt som möjligt
			bestScore = POS_INFINITY;
		}

		try {
			// White to move.
			if(pos.toMove()){
				for (OthelloAction action : possibleActions) {
					stopTimeOfNot();
					OthelloPosition newPos = pos.makeMove(action);

					int score = minValue(newPos, NEG_INFINITY, POS_INFINITY, searchDepth - 1);

					if(score > bestScore) {
						bestScore = score;
						bestAction = action;
					}
				}
			}

			// Black to move.
			else{
				for (OthelloAction action : possibleActions) {
					stopTimeOfNot();
					OthelloPosition newPos = pos.makeMove(action);

					int score = maxValue(newPos, NEG_INFINITY, POS_INFINITY, searchDepth - 1);

					if(score < bestScore){
						bestScore = score;
						bestAction = action;
					}
				}
			}
		}catch (TimeIsUpExeption e){

		}catch (IllegalMoveException e) {
			e.printStackTrace();
		}

		// Om tiden tog slut innan ens ett enda barn blev evaluerat, då ger vi det
		// första lagliga draget.
		if (bestAction == null) {
			return possibleActions.getFirst();
		}

		// returnera bästa draget.
		return bestAction;
	}

	// NY
	private int maxValue(OthelloPosition pos, int alpha, int beta, int depth){
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop at the bottom of the tree or if no possible move is available.
		if(depth == 0 || possibleActions.isEmpty()){
			return evaluator.evaluate(pos);
		}

		int maxVal = NEG_INFINITY;

		// For each possible action.
		for(OthelloAction action : possibleActions) {
			try {
				OthelloPosition newPos = pos.makeMove(action);

				// Saves the biggest value from maxVal and the result from minVal().
				maxVal = Math.max(maxVal, minValue(newPos, alpha, beta, depth - 1));

				// Updates alfa.
				alpha = Math.max(alpha, maxVal);

				// If alfa is greater or equal to beta we can prune.
				if(alpha >= beta){
					break;
				}
			} catch (IllegalMoveException e) {
				// hoppar över barn.
				e.printStackTrace();
			}
		}
		return maxVal;
	}

	// NY
	private int minValue(OthelloPosition pos, int alpha, int beta, int depth){
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop and evaluate at the bottom of the tree or if no possible move is available.
		if(depth == 0 || possibleActions.isEmpty()){
			return evaluator.evaluate(pos);
		}

		int minVal = POS_INFINITY;

		// For each possible action.
		for(OthelloAction action : possibleActions){
			try {
				OthelloPosition newPos = pos.makeMove(action);

				// Saves the smallest value from minVal and the result from maxVal().
				minVal = Math.min(minVal, maxValue(newPos, alpha, beta, depth - 1));

				// Updates beta.
				beta = Math.min(beta, minVal);

				// If alfa is greater or equal to beta we can prune.
				if(alpha >= beta){
					break;
				}
			} catch (IllegalMoveException e) {
				// hoppar över barn.
				e.printStackTrace();
			}

		}
		return minVal;
	}
}