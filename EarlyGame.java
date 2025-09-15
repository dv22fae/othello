public class EarlyGame implements OthelloEvaluator{

    private static final int NUM_WHITE_SQUARES_WEIGHT = 10;
    private static final int CONTROL_MIDDLE_WEIGHT = 25;
    private static final int NUM_CORNER_NEIGHBOURS_WEIGHT = -20;
    private static final int NUM_CORNERS_WEIGHT = 50;
    private static final int NUM_GOOD_EDGE_SQUARES_WEIGHT = 30;

    private int num_bricks = 0;

    private int[][] weightedMatrix;


    // Loads the matrix with weights corresponding to how good squares are to hold.
    private void initializeWeightedMatrix(){
        weightedMatrix = new int[9][9];

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                // First and last row
                if (i == 1 || i == 8){
                    // Corners very good
                    if(j == 1 || j == 8){
                        weightedMatrix[i][j] = 100;
                    }

                    // Corner neighbours very bad as long as you do not have the corner.
                    else if(j == 2 || j == 7){
                        weightedMatrix[i][j] = -10;
                    }

                    // Good square because in order to flip this square the opponent needs to place it in a corner neighbour
                    // which is bad for the opponent since I can take the corner then.
                    else if(j == 3 || j == 6){
                        weightedMatrix[i][j] = 10;
                    }

                    // col 4 and 5 are also good squares.
                    else{
                        weightedMatrix[i][j] = 5;

                    }
                }

                // Second and 7th row are bad squares since they allow opponent to take edge squares.
                else if(i == 2 || i == 7){
                    // Corner neighbours are bad.
                    if(j == 1 || j == 8){
                        weightedMatrix[i][j] = -10;
                    }

                    // Very bad square since it allows opponent to take corner and 2 edge squares.
                    else if(j == 2 || j == 7){
                        weightedMatrix[i][j] = -20;
                    }

                    // Bad square since it allows opponent to grab and edge square.
                    else{
                        weightedMatrix[i][j] = -3;
                    }
                }

                // Good squares to control.
                else if(i == 3 || i == 6){

                    // Very good square because if opponent takes it, it will oen up for you to take corner.
                    if(j == 1 || j == 8){
                        weightedMatrix[i][j] = 10;
                    }

                    // Bad square because it allows opponent to get a good edge square.
                    else if(j == 2 || j == 7){
                        weightedMatrix[i][j] = -3;
                    }

                    // Good square because to take this square the opponent must place a bad square.
                    else if(j == 3 || j == 6){
                        weightedMatrix[i][j] = 7;
                    }

                    // average squares, it allows opponent to take good or bad squares.
                    else{
                        weightedMatrix[i][j] = 1;
                    }

                }

                // Two middle rows are pretty good.
                else{
                    // Edge squares are good, but also it allows opponent to take good edge squares
                    if(j == 1 || j == 8){
                        weightedMatrix[i][j] = 3;
                    }

                    // Bad squares since it opens for taking the edge square for the opponent.
                    else if (j == 2 || j == 7){
                        weightedMatrix[i][j] = -3;
                    }

                    // Rest of the squares are average since they open up for the opponent to get a good or bad square.
                    else{
                        weightedMatrix[i][j] = 1;
                    }

                }
            }
        }
    }

    // Ska den endast evaluera white 'W' till positiv, eller svart med eftersom man spelar som svart i script?
    @Override
    public int evaluate(OthelloPosition pos) {
        initializeWeightedMatrix();

        int totalWeightWhite = 0;
        int totalWeightBlack = 0;

        for (int i = 1; i <= OthelloPosition.BOARD_SIZE; i++) {
            for (int j = 1; j <= OthelloPosition.BOARD_SIZE; j++) {
                if (pos.board[i][j] == 'W'){
                    totalWeightWhite += weightedMatrix[i][j];
                }

                if (pos.board[i][j] == 'B'){
                    totalWeightBlack += weightedMatrix[i][j];
                }
            }
        }

        return totalWeightWhite -totalWeightBlack;

        //OthelloPosition position = (OthelloPosition) pos;

        //return evaluateNumWhiteBricks(position) + evaluateCorners(position) + evaluateCornerNeighbours(position) +evaluateMiddleControlFirstLayer(position) + evaluateGoodEdgeSquares(position);
    }

    public int evaluateNumWhiteBricks(OthelloPosition pos) {
        int blackSquares = 0;
        int whiteSquares = 0;

        for (int i = 1; i <= OthelloPosition.BOARD_SIZE; i++) {
            for (int j = 1; j <= OthelloPosition.BOARD_SIZE; j++) {
                if (pos.board[i][j] == 'W')
                    whiteSquares++;
                    num_bricks++;

                if (pos.board[i][j] == 'B')
                    blackSquares++;
                    num_bricks++;
            }
        }

        return NUM_WHITE_SQUARES_WEIGHT * (whiteSquares - blackSquares);
    }

    private int evaluateMiddleControlFirstLayer(OthelloPosition pos){
        int numMiddleSquares = 0;

        for(int i = 4; i < 6; i++){
            for(int j = 4; j < 6; j++){
                if(pos.board[i][j] == 'W'){
                    numMiddleSquares++;
                }
            }
        }

        return numMiddleSquares * CONTROL_MIDDLE_WEIGHT;
    }

    // Rutor som man vill ha.
    /*
    private int evaluateMiddleControlSecondLayer(OthelloPosition pos){
        int numMiddleSquares = 0;

        for(int i = 3; i < 7; i++){
            for(int j = 3; j < 7; j++){
                if(pos.board[i][j] == 'W'){
                    numMiddleSquares++;
                }
            }
        }

        return numMiddleSquares * CONTROL_MIDDLE_WEIGHT;
    }
*/

    // Position to avoid because they allow opponent to grab a corner.
    // Också lägga till den tredje? alltså pos.board[2][2]?
    private int evaluateCornerNeighbours(OthelloPosition pos){
        int numCornerNeighbours = 0;

        if(pos.board[1][1] != 'W'){
            if(pos.board[2][1] == 'W'){
                numCornerNeighbours++;
            }
            if(pos.board[1][2] == 'W'){
                numCornerNeighbours++;
            }
        }

        if(pos.board[8][1] != 'W'){
            if(pos.board[7][1] == 'W'){
                numCornerNeighbours++;
            }
            if(pos.board[8][2] == 'W'){
                numCornerNeighbours++;
            }
        }

        if(pos.board[8][8] != 'W'){
            if(pos.board[8][7] == 'W'){
                numCornerNeighbours++;
            }
            if(pos.board[7][8] == 'W'){
                numCornerNeighbours++;
            }
        }

        if(pos.board[1][8] != 'W'){
            if(pos.board[2][8] == 'W'){
                numCornerNeighbours++;
            }
            if(pos.board[1][7] == 'W'){
                numCornerNeighbours++;
            }
        }

        return numCornerNeighbours * NUM_CORNER_NEIGHBOURS_WEIGHT;
    }


    // Man vill verkligen ha hörnen.
    private int evaluateCorners(OthelloPosition pos){
        int numCorners = 0;

        if(pos.board[1][1] == 'W'){
            numCorners++;
        }

        if(pos.board[8][1] == 'W'){
            numCorners++;
        }

        if(pos.board[8][8] == 'W'){
            numCorners++;
        }

        if(pos.board[1][8] == 'W'){
            numCorners++;
        }

        return numCorners * NUM_CORNERS_WEIGHT;
    }

    // Bra kantrutor man vill ha så länge man har det hörnet, annars kommer motståndaren kunna få dem.
    private int evaluateGoodEdgeSquares(OthelloPosition pos){
        int numGoodEdgeSquares = 0;

        if(pos.board[1][1] == 'W'){
            for(int i = 2; i < 8; i++){
                if(pos.board[1][i] == 'W'){
                    numGoodEdgeSquares++;
                }
            }
        }

        if(pos.board[1][8] == 'W'){
            for(int i = 2; i < 8; i++){
                if(pos.board[i][8] == 'W'){
                    numGoodEdgeSquares++;
                }
            }
        }

        if(pos.board[8][8] == 'W'){
            for(int i = 2; i < 8; i++){
                if(pos.board[8][i] == 'W'){
                    numGoodEdgeSquares++;
                }
            }
        }

        if(pos.board[8][8] == 'W'){
            for(int i = 2; i < 8; i++){
                if(pos.board[i][1] == 'W'){
                    numGoodEdgeSquares++;
                }
            }
        }

        return numGoodEdgeSquares * NUM_GOOD_EDGE_SQUARES_WEIGHT;
    }
}
