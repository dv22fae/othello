/**
 * This interface defines the mandatory methods for an evaluator, i.e., a class
 * that can take a position and return an integer value that represents a
 * heuristic evaluation of the position (positive numbers if the position is
 * better for the first player, white). Notice that an evaluator is not supposed
 * to make moves in the position to 'see into the future', but only evaluate the
 * static features of the postion.
 *
 * @author Henrik Björklund, original
 * @author Fredrik Alexandre, co-author
 * @author Samuel Hagner, co-author
 *
 * @version 2.0, 2025-09-24
 */

public interface OthelloEvaluator {

	/**
	 * Reterns an integer, representing a heuristic evaluation of the postion.
	 *
	 * @param position, position to evaluate.
	 * @return score where higher is better for White and lower is better for Black.
	 */
	public int evaluate(OthelloPosition position);

}