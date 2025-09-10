public class EarlyGame implements OthelloEvaluator{

    @Override
    public int evaluate(OthelloPosition pos) {
        OthelloPosition position = (OthelloPosition) pos;
        int blackSquares = 0;
        int whiteSquares = 0;
        int numSquaresWeight = 10;

        for (int i = 1; i <= OthelloPosition.BOARD_SIZE; i++) {
            for (int j = 1; j <= OthelloPosition.BOARD_SIZE; j++) {
                if (position.board[i][j] == 'W')
                    whiteSquares++;
                if (position.board[i][j] == 'B')
                    blackSquares++;
            }
        }

        return (numSquaresWeight * (whiteSquares - blackSquares)) + evaluateMiddleControl(position);
    }

    private int evaluateMiddleControlFirstLayer(OthelloPosition pos){
        int controlMiddleWeight = 20;
        int numMiddleSquares = 0;

        for(int i = 4; i < 6; i++){
            for(int j = 4; j < 6; j++){
                if(pos.board[i][j] == 'W'){
                    numMiddleSquares++;
                }
            }
        }
/*
        if(pos.board[4][4] == 'W'){
            numMiddleSquares++;
        }

        if(pos.board[5][4] == 'W'){
            numMiddleSquares++;
        }

        if(pos.board[4][5] == 'W'){
            numMiddleSquares++;
        }

        if(pos.board[5][5] == 'W'){
            numMiddleSquares++;
        }
*/
        return numMiddleSquares * controlMiddleWeight;
    }

    // Rutor som man vill ha.
    private int evaluateMiddleControlSecondLayer(OthelloPosition pos){
        int controlMiddleWeight = 20;
        int numMiddleSquares = 0;

        for(int i = 3; i < 7; i++){
            for(int j = 3; j < 7; j++){
                if(pos.board[i][j] == 'W'){
                    numMiddleSquares++;
                }
            }
        }

        return numMiddleSquares * controlMiddleWeight;
    }


    // Position to avoid because they allow opponent to grab a corner.
    // Också lägga till den tredje? alltså pos.board[2][2]?
    private int evaluateCornerNeighbours(OthelloPosition pos){
        int numCornerNeighbours = 0;
        int numCornerNeighboursWeight = -30;

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

        return numCornerNeighbours * numCornerNeighboursWeight;
    }


    // Man vill verkligen ha hörnen.
    private int evaluateCorners(OthelloPosition pos){
        int numCorners = 0;
        int numCornersWeight = 50;

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

        return numCorners * numCornersWeight;
    }

    // inte klar.
    private int evaluateGoodEdgeSquares(OthelloPosition pos){
        int numGoodEdgeSquares = 0;
        int numGoodEdgeSquaresWeight = 30;

        return numGoodEdgeSquares * numGoodEdgeSquaresWeight;
    }





}
