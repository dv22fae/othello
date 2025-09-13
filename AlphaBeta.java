import java.util.LinkedList;
import java.util.Timer;

/**
 * This is where you implement the alpha-beta algorithm.
 * See <code>OthelloAlgorithm</code> for detailss
 *
 * @author Henrik Bj&ouml;rklund
 *
 */
public class AlphaBeta implements OthelloAlgorithm {

	private static final int NEG_INFINITY = Integer.MIN_VALUE;
	private static final int POS_INFINITY = Integer.MAX_VALUE;


	protected int searchDepth;
	protected static final int DefaultDepth = 7;
	protected OthelloEvaluator evaluator;

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

		OthelloAction bestAction = null;

		// Keeps track on the best action from the root (current position).
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop at the bottom of the tree or if no possible move is available.
		if(possibleActions.isEmpty()){
			return new OthelloAction("pass");
		}

		// White to move.
		if(pos.toMove()){
			int bestScore = NEG_INFINITY;
			for (OthelloAction action : possibleActions) {
				try {
					OthelloPosition newPos = pos.makeMove(action);

					int score = minValue(newPos, NEG_INFINITY, POS_INFINITY, searchDepth - 1);

					if(score > bestScore){
						bestScore = score;
						bestAction = action;
					}
				} catch (IllegalMoveException e){
					e.printStackTrace();
				}
			}
		}

		// Black to move.
		else{
			int bestScore = POS_INFINITY;
			for (OthelloAction action : possibleActions) {
				try {
					OthelloPosition newPos = pos.makeMove(action);

					int score = maxValue(newPos, NEG_INFINITY, POS_INFINITY, searchDepth - 1);

					if(score < bestScore){
						bestScore = score;
						bestAction = action;
					}
				}catch (IllegalMoveException e){
					e.printStackTrace();
				}
			}
		}
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