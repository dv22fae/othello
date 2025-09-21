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
	 * Constructor that makes a alphaBeta with default evaluator
	 * and the default search depth.
	 */
	public AlphaBeta() {
		evaluator = new CountingEvaluator();
		searchDepth = DefaultDepth;
	}

	/**
	 * Constructor that makes a AlphaBeta with a given evaluator
	 * and the default search depth.
	 *
	 * @param eval, evaluator used to score positions.
	 */
	public AlphaBeta(OthelloEvaluator eval) {
		evaluator = eval;
		searchDepth = DefaultDepth;
	}

	/**
	 * Constructor that makes a AlphaBeta with a given evaluator
	 * and with a given search depth.
	 *
	 * @param eval, evaluator used to score positions.
	 * @param depth, evaluator used to score positions.
	 */
	public AlphaBeta(OthelloEvaluator eval, int depth) {
		evaluator = eval;
		searchDepth = depth;
	}

	/**
	 * Sets evaluator to be used for scoring positions.
	 *
	 * @param eval, given to be used evaluator.
	 */
	public void setEvaluator(OthelloEvaluator eval) {
		evaluator = eval;
	}

	/**
	 * Sets evaluator to be used for scoring positions.
	 *
	 * @param depth, given to be used evaluator.
	 */
	public void setSearchDepth(int depth) {
		searchDepth = depth;
	}

	/**
	 * Picks the best move from the given position by alphaBeta search.
	 *
	 * If there is no legal moves the method will return pass.
	 * If time runs out before any better move is evaluated it returns the first legal move.
	 *
	 * @param pos, current game position.
	 * @return best action.
	 */
	public OthelloAction evaluate(OthelloPosition pos) {
		stopTimeOfNot();
		OthelloAction bestAction = null;

		// Keeps track on the best action from the root (current position).
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop at the bottom of the tree or if no possible move is available.
		if(possibleActions.isEmpty()){
			return new OthelloAction("pass");
		}

		// Initiate the best score at the beginning from which color is to play.
		int bestScore = bestScoreAtstart(pos);

		try {
			// White to move.
			if (pos.toMove()) {
				for (OthelloAction action : possibleActions) {
					stopTimeOfNot();
					OthelloPosition newPos = pos.makeMove(action);

					int score = minValue(newPos, NEG_INFINITY, POS_INFINITY, searchDepth - 1);

					if (score > bestScore) {
						bestScore = score;
						bestAction = action;
					}
				}
			}

			// Black to move.
			else {
				for (OthelloAction action : possibleActions) {
					stopTimeOfNot();
					OthelloPosition newPos = pos.makeMove(action);

					int score = maxValue(newPos, NEG_INFINITY, POS_INFINITY, searchDepth - 1);

					if (score < bestScore) {
						bestScore = score;
						bestAction = action;
					}
				}
			}
		} catch (TimeIsUpExeption e) {
			System.err.println("Time limit is up");
		} catch (IllegalMoveException e) {
			System.err.println("Skipping illegal move");
		}

		// If the time ran out before anything could be evaluated.
		if (bestAction == null) {
			return possibleActions.getFirst();
		}

		// Return the best move.
		return bestAction;
	}

	/**
	 * Part of alfaBeta for the maximizing player, white player.
	 *
	 * Looks forward and hands back the highest score found.
	 * The method also uses alpha and beta to cut branches
	 * that will not make a better result.
	 *
	 * @param pos, position to evaluate from.
	 * @param alpha, current best lower bound.
	 * @param beta, current best upper bound.
	 * @param depth, depth to search.
	 * @return score for white player.
	 */
	private int maxValue(OthelloPosition pos, int alpha, int beta, int depth) {
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop at the bottom of the tree or if no possible move is available.
		if(depth == 0 || possibleActions.isEmpty()) {
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
				if (alpha >= beta) {
					break;
				}
			} catch (IllegalMoveException e) {
				// hoppar över barn.
				e.printStackTrace();
			}
		}
		return maxVal;
	}

	/**
	 * Part of alfaBeta for the minimizing player, black player.
	 *
	 * Looks forward and hands back the lowest score found.
	 * The method also uses alpha and beta to cut branches
	 * that will not make a better result.
	 *
	 * @param pos, position to evaluate from.
	 * @param alpha, current best lower bound.
	 * @param beta, current best upper bound.
	 * @param depth, depth to search.
	 * @return score for black player.
	 */
	private int minValue(OthelloPosition pos, int alpha, int beta, int depth) {
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop and evaluate at the bottom of the tree or if no possible move is available.
		if(depth == 0 || possibleActions.isEmpty()) {
			return evaluator.evaluate(pos);
		}

		int minVal = POS_INFINITY;

		// For each possible action.
		for(OthelloAction action : possibleActions) {
			try {
				OthelloPosition newPos = pos.makeMove(action);

				// Saves the smallest value from minVal and the result from maxVal().
				minVal = Math.min(minVal, maxValue(newPos, alpha, beta, depth - 1));

				// Updates beta.
				beta = Math.min(beta, minVal);

				// If alfa is greater or equal to beta we can prune.
				if(alpha >= beta) {
					break;
				}
			} catch (IllegalMoveException e) {
				// hoppar över barn.
				e.printStackTrace();
			}

		}
		return minVal;
	}

	/**
	 * Initial best score at the root:
	 *
	 * @param pos, position
	 * @return positive or negative infinity.
	 */
	private int bestScoreAtstart(OthelloPosition pos) {
		if (pos.toMove()) {
			// Begin so low as possible.
			return NEG_INFINITY;
		} else {
			// Begin so high as possible.
			return POS_INFINITY;
		}
	}
}