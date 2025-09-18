import java.util.LinkedList;

public class EarlyGame implements OthelloEvaluator{

    private static final int NUM_WHITE_SQUARES_WEIGHT = 10;
    private static final int CONTROL_MIDDLE_WEIGHT = 25;
    private static final int NUM_CORNER_NEIGHBOURS_WEIGHT = -20;
    private static final int NUM_CORNERS_WEIGHT = 50;
    private static final int NUM_GOOD_EDGE_SQUARES_WEIGHT = 30;

    private int num_bricks = 0;

    private int[][] weightedMatrix;

    /* Ideer, lägg weightedMatrix i Othelloposition och initialize den en gång i Othello.
    Sedan använder man pos.weightedMatrix här så sliper man skapa den på nytt varje evaluering.

    Man kan också bygga in antalet drag man har tillgång till vid varje position för att det är bra
    så man har mycket valbarheter.

    Också i position kan man ha koll på hur många brickor som är lagda för att avgöra om det är
    early game, middlegame eller lategame.

    Vid väldigt lategame så borde antalet brickor vara viktigare än vikterna i matrisen?

    Kanske ta bort middle game och ha endast early game och lategame?
     */
    // Ska den endast evaluera white 'W' till positiv, eller svart med eftersom man spelar som svart i script?
    @Override
    public int evaluate(OthelloPosition pos) {
        int whiteNumMoves = 0;
        int blackNumMoves = 0;

        LinkedList<OthelloAction> actions = pos.getMoves();

        // White
        /*if(pos.maxPlayer){
            if(actions != null){
                whiteNumMoves = actions.size();
            }
        }*/

        // Black
        /*else{
            if(actions != null){
                blackNumMoves = actions.size();
            }
        }*/


        initializeWeightedMatrix(pos);
        //weightedMatrixPrint();

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

        // HMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM kanske inte helt korrekt hmmmmmmmmmmmmmmmmmm.
        // Kanske måste att svart ska försöka få så högt som möjligt när vi är svart,
        // Å samma på vit.
        return totalWeightWhite - totalWeightBlack;

        //OthelloPosition position = (OthelloPosition) pos;

        //return evaluateNumWhiteBricks(position) + evaluateCorners(position) + evaluateCornerNeighbours(position) +evaluateMiddleControlFirstLayer(position) + evaluateGoodEdgeSquares(position);
    }



    // Loads the matrix with weights corresponding to how good squares are to hold.
    private void initializeWeightedMatrix(OthelloPosition pos){
        weightedMatrix = new int[9][9];

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {

                // First and last row are generally very good squares
                if (i == 1 || i == 8){
                    matrixInitializeFirstAndLastRow(i, j, pos);
                }

                // Second and 7th row are bad squares since they allow opponent to take edge squares.
                else if(i == 2 || i == 7){
                    matrixInitializeSecondAndSecondLastRow(i, j, pos);
                }

                // Good squares to control.
                else if(i == 3 || i == 6){
                    matrixInitializeThirdAndThirdLastRow(i, j);
                }

                // Two middle rows are pretty good squares.
                else{
                    matrixInitializeMiddleRows(i, j);
                }
            }
        }
    }

    private void matrixInitializeFirstAndLastRow(int i, int j, OthelloPosition pos){
        // Corners are very good
        if(j == 1 || j == 8){
            weightedMatrix[i][j] = 100;
        }

        // Corner neighbours are bad as long as you do not have the corner.
        else if(j == 2){

            // If you already own the corner then this square is good, otherwise it is bad.
            if(pos.board[i][j - 1] == 'W'){
                weightedMatrix[i][j] = 7;
            }
            else{
                weightedMatrix[i][j] = -10;
            }
        }

        else if(j == 7){
            // If you already own the corner then this square is good, otherwise it is bad.
            if(pos.board[i][j + 1] == 'W'){
                weightedMatrix[i][j] = 7;
            }
            else{
                weightedMatrix[i][j] = -10;
            }
        }

        // Good square because in order to flip this square the opponent needs to place it in a bad square.
        else if(j == 3){
            // If you already own the corner it is still a good square, but less good.
            if(pos.board[i][j - 2] == 'W'){
                weightedMatrix[i][j] = 5;
            }
            else{
                weightedMatrix[i][j] = 10;
            }
        }

        else if(j == 6){
            // If you already own the corner it is still a good square, but less good.
            if(pos.board[i][j + 2] == 'W'){
                weightedMatrix[i][j] = 5;
            }
            else{
                weightedMatrix[i][j] = 10;
            }
        }

        // col 4 and 5 are good squares.
        else if(j == 4 || j == 5){

            // If the neighbours to the side are also white the square becomes better.
            // Maybe check all the way to corners?
            if(weightedMatrix[i][j - 1] == 'W' && weightedMatrix[i][j + 1] == 'W'){
                weightedMatrix[i][j] = 5;
            }
            else{
                weightedMatrix[i][j] = 5;
            }
        }
    }

    private void matrixInitializeSecondAndSecondLastRow(int i, int j, OthelloPosition pos){
        // Corner neighbours are bad because it can allow opponent to take a corner.
        if(j == 1){

            // If the corner is already taken it is a good square.
            if(pos.board[i - 1][j] == 'W'){
                weightedMatrix[i][j] = 7;
            }
            else{
                weightedMatrix[i][j] = -10;
            }
        }
        else if(j == 8){

            // If the corner is already taken it is a good square.
            if(pos.board[i - 1][j] == 'W'){
                weightedMatrix[i][j] = 7;
            }
            else{
                weightedMatrix[i][j] = -10;
            }
        }

        // Very bad square since it allows opponent to take corner and 2 edge squares.
        else if(j == 2){

            // If you have the corner and the pos to the left and the pos upwards then it's a decent square.
            if(pos.board[i][j - 1] == 'W' && pos.board[i - 1][j] == 'W' && pos.board[i - 1][j - 1] == 'W'){
                weightedMatrix[i][j] = 3;
            }
            else{
                weightedMatrix[i][j] = -20;
            }
        }

        // Very bad square since it allows opponent to take corner and 2 edge squares.
        else if(j == 7){

            // If you have the corner and the pos to the left and the pos upwards then it's a decent square.
            if(pos.board[i][j + 1] == 'W' && pos.board[i + 1][j] == 'W' && pos.board[i - 1][j + 1] == 'W'){
                weightedMatrix[i][j] = 3;
            }
            else{
                weightedMatrix[i][j] = -20;
            }
        }

        // Bad squares since it allows opponent to grab and edge square.
        else{

            // If the corresponding edge square is already take the square is decent.
            if(pos.board[i - 1][j] == 'W'){
                weightedMatrix[i][j] = 3;
            }
            else{
                weightedMatrix[i][j] = -3;
            }
        }
    }

    // Ej uppdaterad.
    private void matrixInitializeThirdAndThirdLastRow(int i, int j){
        // Very good square because if opponent takes it, it will open up for you to take corner.
        if(j == 1 || j == 8){
            weightedMatrix[i][j] = 10;
        }

        // Bad square because it allows opponent to get a good edge square.
        else if(j == 2 || j == 7){
            weightedMatrix[i][j] = -3;
        }

        // Good square because to take this square the opponent must almost always place a bad square.
        else if(j == 3 || j == 6){
            weightedMatrix[i][j] = 7;
        }

        // average squares, it allows opponent to take either good or bad squares.
        else{
            weightedMatrix[i][j] = 1;
        }
    }

    private void matrixInitializeMiddleRows(int i, int j){
        // Edge squares are good, but it also allows opponent to take good edge squares
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

    private void weightedMatrixPrint(){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                System.out.println(i + "," + j + "      " + weightedMatrix[i][j]);
            }
        }
    }
}
