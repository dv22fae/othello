
/**
 * Early game heuristic evaluator for Othello.
 *
 * Scores a position by summing a weight distrobution inspired by "xxxx" for every occupied cell.
 *
 * Positive weights for strong squares,
 * negative weights for risky and lower priority squares.
 *
 * Reference:
 *
 * @author Fredrik Alexandre
 * @author Samuel Hagner
 * @version 1.0, 2025-09-24
 */

public class Heuristics implements OthelloEvaluator{

    /**
     * Matrix to handle weight of each position in the matrix.
     */
    private int[][] weightedMatrix;

    /**
     * Evaluates the given position.
     *
     * Builds the weight matrix and returns a score.
     *
     * @param pos, position to evaluate.
     * @return integer score, higher is better for White, lower is better for Black.
     */
    @Override
    public int evaluate(OthelloPosition pos) {

        initializeWeightedMatrix(pos);

        int totalWeightWhite = 0;
        int totalWeightBlack = 0;

        for (int i = 1; i <= OthelloPosition.BOARD_SIZE; i++) {
            for (int j = 1; j <= OthelloPosition.BOARD_SIZE; j++) {
                if (pos.board[i][j] == 'W') {
                    totalWeightWhite += weightedMatrix[i][j];
                }

                if (pos.board[i][j] == 'B') {
                    totalWeightBlack += weightedMatrix[i][j];
                }
            }
        }

        return totalWeightWhite - totalWeightBlack;
    }

    /**
     * Builds the weight matrix (stored as 9x9 for 1-based indexing).
     *
     * Loads the matrix with weights corresponding to how good squares are to hold.
     *
     * @param pos, position used for to check around corners and edges.
     */
    private void initializeWeightedMatrix(OthelloPosition pos) {
        weightedMatrix = new int[9][9];

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {

                // First and last row are generally very good squares
                if (i == 1 || i == 8) {
                    matrixInitializeFirstAndLastRow(i, j, pos);
                }

                // Second and 7th row are bad squares since they allow opponent to take edge squares.
                else if(i == 2 || i == 7) {
                    matrixInitializeSecondAndSecondLastRow(i, j, pos);
                }

                // Good squares to control.
                else if(i == 3 || i == 6) {
                    matrixInitializeThirdAndThirdLastRow(i, j, pos);
                }

                // Two middle rows are pretty good squares.
                else {
                    matrixInitializeMiddleRows(i, j, pos);
                }
            }
        }
    }

    /**
     * Assigns weights for the first, row 1, and last, row 8, rows.
     *
     * @param i, row.
     * @param j, column.
     * @param pos, position used to see which color has a squere.
     */
    private void matrixInitializeFirstAndLastRow(int i, int j, OthelloPosition pos) {
        switch (j) {
            // Corners are very good squares
            case 1:
            case 8: {
                weightedMatrix[i][j] = 120;
                break;
            }

            case 2: {
                // If you already own the corner then this square is good, otherwise it is bad.
                if (pos.isOwnSquare(i, j - 1)) {
                    weightedMatrix[i][j] = 20;
                }
                else{
                    weightedMatrix[i][j] = -20;
                }
                break;
            }

            case 7: {
                // If you already own the corner then this square is good, otherwise it is bad.
                if (pos.isOwnSquare(i, j + 1)) {
                    weightedMatrix[i][j] = 20;
                }
                else {
                    weightedMatrix[i][j] = -20;
                }
                break;
            }

            case 3: {
                // If you already own the corner and the neighbour to the left, it is a good square.
                if (pos.isOwnSquare(i, j - 2) && pos.isOwnSquare(i, j - 1)) {
                    weightedMatrix[i][j] = 20;
                }
                else {
                    weightedMatrix[i][j] = 20;
                }

                // If the square to the left is empty or the opponent square, this square is bad.
                if(!pos.isOwnSquare(i, j - 1)){
                    weightedMatrix[i][j] = -10;
                }
                break;
            }

            case 6: {
                // If you already own the corner and the neighbour to the right, it is a good square.
                if (pos.isOwnSquare(i, j + 2) && pos.isOwnSquare(i, j + 1)) {
                    weightedMatrix[i][j] = 20;
                }
                else {
                    weightedMatrix[i][j] = 20;
                }

                // If the square to the right is empty or the opponent square, this square is bad.
                if (!pos.isOwnSquare(i, j + 1)) {
                    weightedMatrix[i][j] = -10;
                }
                break;
            }

            // The centre square are decent squares.
            case 4:
            case 5: {
                weightedMatrix[i][j] = 5;
            }

            default:
                break;
        }

    }

    /**
     * Assigns weights for row 2, and last, row 7, rows.
     *
     * @param i, row.
     * @param j, column.
     * @param pos, position used to see which color has a squere.
     */
    private void matrixInitializeSecondAndSecondLastRow(int i, int j, OthelloPosition pos){
        // Corner neighbours are bad because it can allow opponent to take a corner.
        if (i == 2) {
            switch (j) {
                case 1:
                case 8: {
                    // If the corner is already taken it is a good square, else it is a bad square.
                    if (pos.isOwnSquare(i - 1, j)) {
                        weightedMatrix[i][j] = 20;
                    }
                    else{
                        weightedMatrix[i][j] = -20;
                    }
                    break;
                }

                case 2: {
                    // If you have the corner and the pos to the left and the pos upwards then it's a decent square. Otherwise, a bad square.
                    if (pos.isOwnSquare(i, j - 1) && pos.isOwnSquare(i - 1, j) && pos.isOwnSquare(i - 1, j - 1)) {
                        weightedMatrix[i][j] = 5;
                    }
                    else{
                        weightedMatrix[i][j] = -40;
                    }
                    break;
                }

                case 7: {
                    // If you have the corner and the pos to the right and the pos upwards then it's a decent square. Otherwise, a bad square.
                    if (pos.isOwnSquare(i, j + 1) && pos.isOwnSquare(i - 1, j) && pos.isOwnSquare(i - 1, j + 1)) {
                        weightedMatrix[i][j] = 5;
                    }
                    else {
                        weightedMatrix[i][j] = -40;
                    }
                    break;
                }

                default: {
                    // The squares in the middle are bad square as long as you do not have the edge-square above.
                    if (pos.isOwnSquare(i - 1, j)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else {
                        weightedMatrix[i][j] = -5;
                    }
                    break;
                }
            }
        }

        else if (i == 7) {
            switch (j) {
                case 1:
                case 8: {
                    // If the corner is already taken it is a good square.
                    if (pos.isOwnSquare(i + 1, j)) {
                        weightedMatrix[i][j] = 20;
                    }
                    else {
                        weightedMatrix[i][j] = -20;
                    }
                    break;
                }

                case 2: {
                    // If you have the corner and the pos to the left and the pos downwards then it's a decent square. Otherwise, a bad square.
                    if (pos.isOwnSquare(i, j - 1) && pos.isOwnSquare(i + 1, j) && pos.isOwnSquare(i + 1, j - 1)) {
                        weightedMatrix[i][j] = 5;
                    }
                    else {
                        weightedMatrix[i][j] = -40;
                    }
                    break;
                }

                case 7: {
                    // If you have the corner and the pos to the right and the pos downwards then it's a decent square. Otherwise, a bad square.
                    if (pos.isOwnSquare(i, j + 1) && pos.isOwnSquare(i + 1, j) && pos.isOwnSquare(i + 1, j + 1)) {
                        weightedMatrix[i][j] = 5;
                    }
                    else {
                        weightedMatrix[i][j] = -40;
                    }
                    break;
                }

                default: {
                    // The squares in the middle are bad square as long as you do not have the edge-square below.
                    if (pos.isOwnSquare(i + 1, j)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else {
                        weightedMatrix[i][j] = -5;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Assigns weights for row 3, and row 6, rows.
     *
     * @param i, row.
     * @param j, column.
     * @param pos, position used to see which color has a squere.
     */
    private void matrixInitializeThirdAndThirdLastRow(int i, int j, OthelloPosition pos) {
        if (i == 3) {
            switch (j) {
                case 1:
                case 8: {
                    // Good square if opponent does not have the corner,
                    // because if opponent takes it, it will open up for you to take corner.
                    if (pos.isOpponentSquare(i - 2, j)) {
                        weightedMatrix[i][j] = -5;
                    } else {
                        weightedMatrix[i][j] = 20;
                    }
                    break;
                }

                case 2: {
                    // Bad square if you do not have the edge-square to the left, otherwise an okay square.
                    if (pos.isOwnSquare(i, j - 1)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else {
                        weightedMatrix[i][j] = -5;
                    }
                    break;
                }

                case 7: {
                    // Bad square if you do not have the edge-square to the right, otherwise an okay square.
                    if (pos.isOwnSquare(i, j + 1)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else {
                        weightedMatrix[i][j] = -5;
                    }
                    break;
                }

                // Good squares because it allows the opponent to take bad squares.
                case 3:
                case 6: {
                    weightedMatrix[i][j] = 15;
                    break;
                }

                // Middle square are decent squares.
                default: {
                    weightedMatrix[i][j] = 3;
                    break;
                }

            }
        }

        else if (i == 6) {
            switch (j) {
                case 1:
                case 8: {
                    // Good square if opponent does not have the corner, because if opponent takes it, it will open up for you to take corner.
                    if (pos.isOpponentSquare(i + 2, j)) {
                        weightedMatrix[i][j] = -5;
                    }
                    else{
                        weightedMatrix[i][j] = 20;
                    }
                    break;
                }

                case 2: {
                    // Bad square if you do not have the edge-square to the left, otherwise an okay square.
                    if (pos.isOwnSquare(i, j - 1)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else {
                        weightedMatrix[i][j] = -5;
                    }
                    break;
                }

                case 7: {
                    // Bad square if you do not have the edge-square to the right, otherwise an okay square.
                    if (pos.isOwnSquare(i, j + 1)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else {
                        weightedMatrix[i][j] = -5;
                    }
                    break;
                }

                // Good squares because it allows the opponent to take bad squares.
                case 3:
                case 6: {
                    weightedMatrix[i][j] = 15;
                    break;
                }

                // Middle square are decent squares.
                default: {
                    weightedMatrix[i][j] = 3;
                    break;
                }
            }
        }
    }

    /**
     * Assigns weights for row 4, and row 5, rows.
     *
     * @param i, row.
     * @param j, column.
     * @param pos, position used to see which color has a squere.
     */
    private void matrixInitializeMiddleRows(int i, int j, OthelloPosition pos){
        switch (j) {
            // Edge squares are good.
            case 1:
            case 8: {
                weightedMatrix[i][j] = 5;
            }

            case 2: {
                // Good square if the edge-square to the left is yours, otherwise a bad square.
                if (pos.isOwnSquare(i, j - 1 )) {
                    weightedMatrix[i][j] = 3;
                }
                else {
                    weightedMatrix[i][j] = -5;
                }
                break;
            }

            case 7: {
                // Good square if the edge-square to the right is yours, otherwise a bad square.
                if (pos.isOwnSquare(i, j + 1 )) {
                    weightedMatrix[i][j] = 3;
                }
                else {
                    weightedMatrix[i][j] = -5;
                }
                break;
            }

            // Rest of the squares are decent squares.
            default: {
                weightedMatrix[i][j] = 3;
                break;
            }
        }
    }

    /**
     * For debugging.
     */
    private void weightedMatrixPrint() {
        for (int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                System.out.println(i + "," + j + "      " + weightedMatrix[i][j]);
            }
        }
    }
}