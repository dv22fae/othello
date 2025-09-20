/**
 * A simple evaluator that just counts the number of black and white squares
 * 
 * @author Henrik Bj&ouml;rklund
 */
public class CountingEvaluator implements OthelloEvaluator {

	/**
	 * Calculates a score for a given position by counting squares for black and white player.
	 *
	 * It counts all white and black squares on the board and returns
	 * whiteSquares - blackSquares. A higher value is good for White
	 * and a negative value favors Black.
	 *
	 * @param pos, position to evaluate.
	 * @return difference, whiteSquares - blackSquares.
	 */
	public int evaluate(OthelloPosition pos) {
		OthelloPosition position = (OthelloPosition) pos;
		int blackSquares = 0;
		int whiteSquares = 0;
		for (int i = 1; i <= OthelloPosition.BOARD_SIZE; i++) {
			for (int j = 1; j <= OthelloPosition.BOARD_SIZE; j++) {
				if (position.board[i][j] == 'W')
					whiteSquares++;
				if (position.board[i][j] == 'B')
					blackSquares++;
			}
		}
		return whiteSquares - blackSquares;
	}
}