public class EarlyGame implements OthelloEvaluator{

    private static final int NUM_WHITE_SQUARES_WEIGHT = 10;
    private static final int CONTROL_MIDDLE_WEIGHT = 25;
    private static final int NUM_CORNER_NEIGHBOURS_WEIGHT = -20;
    private static final int NUM_CORNERS_WEIGHT = 50;
    private static final int NUM_GOOD_EDGE_SQUARES_WEIGHT = 30;

    // Ska den endast evaluera white 'W' till positiv, eller svart med eftersom man spelar som svart i script?
    @Override
    public int evaluate(OthelloPosition pos) {
        OthelloPosition position = (OthelloPosition) pos;

        return evaluateNumWhiteBricks(position) + evaluateCorners(position) + evaluateCornerNeighbours(position) +evaluateMiddleControlFirstLayer(position) + evaluateGoodEdgeSquares(position);
    }

    public int evaluateNumWhiteBricks(OthelloPosition pos) {
        int blackSquares = 0;
        int whiteSquares = 0;

        for (int i = 1; i <= OthelloPosition.BOARD_SIZE; i++) {
            for (int j = 1; j <= OthelloPosition.BOARD_SIZE; j++) {
                if (pos.board[i][j] == 'W')
                    whiteSquares++;
                if (pos.board[i][j] == 'B')
                    blackSquares++;
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
