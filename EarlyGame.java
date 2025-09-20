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
        //int numPossibleMoves = 0;
        //LinkedList<OthelloAction> actions = pos.getMoves();
        //numPossibleMoves = actions.size();

        initializeWeightedMatrix(pos);

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

        return totalWeightWhite - totalWeightBlack;
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
                    matrixInitializeThirdAndThirdLastRow(i, j, pos);
                }

                // Two middle rows are pretty good squares.
                else{
                    matrixInitializeMiddleRows(i, j, pos);
                }
            }
        }
    }

    private void matrixInitializeFirstAndLastRow(int i, int j, OthelloPosition pos){
        switch (j){
            // Corners are very good squares
            case 1, 8 -> weightedMatrix[i][j] = 120;

            case 2 ->{
                // If you already own the corner then this square is good, otherwise it is bad.
                if(pos.isOwnSquare(i, j - 1)){
                    weightedMatrix[i][j] = 20;
                }
                else{
                    weightedMatrix[i][j] = -20;
                }
            }

            case 7 ->{
                // If you already own the corner then this square is good, otherwise it is bad.
                if(pos.isOwnSquare(i, j + 1)){
                    weightedMatrix[i][j] = 20;
                }
                else{
                    weightedMatrix[i][j] = -20;
                }
            }

            case 3 ->{
                // If you already own the corner and the neighbour to the left, it is a good square.
                if(pos.isOwnSquare(i, j - 2) && pos.isOwnSquare(i, j - 1)){
                    weightedMatrix[i][j] = 20;
                }
                else{
                    weightedMatrix[i][j] = 20;
                }

                // If the square to the left is empty or the opponent square, this square is bad.
                if(!pos.isOwnSquare(i, j - 1)){
                    weightedMatrix[i][j] = -10;
                }
            }

            case 6 ->{
                // If you already own the corner and the neighbour to the right, it is a good square.
                if(pos.isOwnSquare(i, j + 2) && pos.isOwnSquare(i, j + 1)){
                    weightedMatrix[i][j] = 20;
                }
                else{
                    weightedMatrix[i][j] = 20;
                }

                // If the square to the right is empty or the opponent square, this square is bad.
                if(!pos.isOwnSquare(i, j + 1)){
                    weightedMatrix[i][j] = -10;
                }
            }

            // The centre square are decent squares.
            case 4, 5 -> weightedMatrix[i][j] = 5;
        }
    }

    private void matrixInitializeSecondAndSecondLastRow(int i, int j, OthelloPosition pos){
        // Corner neighbours are bad because it can allow opponent to take a corner.
        if(i == 2){
            switch (j){
                case 1, 8 ->{
                    // If the corner is already taken it is a good square, else it is a bad square.
                    if(pos.isOwnSquare(i - 1, j)){
                        weightedMatrix[i][j] = 20;
                    }
                    else{
                        weightedMatrix[i][j] = -20;
                    }
                }

                case 2 ->{
                    // If you have the corner and the pos to the left and the pos upwards then it's a decent square. Otherwise, a bad square.
                    if(pos.isOwnSquare(i, j - 1) && pos.isOwnSquare(i - 1, j) && pos.isOwnSquare(i - 1, j - 1)){
                        weightedMatrix[i][j] = 5;
                    }
                    else{
                        weightedMatrix[i][j] = -40;
                    }
                }

                case 7 ->{
                    // If you have the corner and the pos to the right and the pos upwards then it's a decent square. Otherwise, a bad square.
                    if(pos.isOwnSquare(i, j + 1) && pos.isOwnSquare(i - 1, j) && pos.isOwnSquare(i - 1, j + 1)){
                        weightedMatrix[i][j] = 5;
                    }
                    else{
                        weightedMatrix[i][j] = -40;
                    }
                }

                default -> {
                    // The squares in the middle are bad square as long as you do not have the edge-square above.
                    if(pos.isOwnSquare(i - 1, j)){
                        weightedMatrix[i][j] = 3;
                    }
                    else{
                        weightedMatrix[i][j] = -5;
                    }
                }
            }
        }

        else if(i == 7){
            switch (j){
                case 1, 8 ->{
                    // If the corner is already taken it is a good square.
                    if(pos.isOwnSquare(i + 1, j)){
                        weightedMatrix[i][j] = 20;
                    }
                    else{
                        weightedMatrix[i][j] = -20;
                    }
                }

                case 2 ->{
                    // If you have the corner and the pos to the left and the pos downwards then it's a decent square. Otherwise, a bad square.
                    if(pos.isOwnSquare(i, j - 1) && pos.isOwnSquare(i + 1, j) && pos.isOwnSquare(i + 1, j - 1)){
                        weightedMatrix[i][j] = 5;
                    }
                    else{
                        weightedMatrix[i][j] = -40;
                    }
                }

                case 7 ->{
                    // If you have the corner and the pos to the right and the pos downwards then it's a decent square. Otherwise, a bad square.
                    if(pos.isOwnSquare(i, j + 1) && pos.isOwnSquare(i + 1, j) && pos.isOwnSquare(i + 1, j + 1)){
                        weightedMatrix[i][j] = 5;
                    }
                    else{
                        weightedMatrix[i][j] = -40;
                    }
                }

                default -> {
                    // The squares in the middle are bad square as long as you do not have the edge-square below.
                    if(pos.isOwnSquare(i + 1, j)) {
                        weightedMatrix[i][j] = 3;
                    }
                    else{
                        weightedMatrix[i][j] = -5;
                    }
                }
            }
        }
    }


    private void matrixInitializeThirdAndThirdLastRow(int i, int j, OthelloPosition pos){
        if(i == 3){
            switch (j){
                case 1, 8 -> {
                    // Good square if opponent does not have the corner, because if opponent takes it, it will open up for you to take corner.
                    if (pos.isOpponentSquare(i - 2, j)) {
                        weightedMatrix[i][j] = -5;
                    } else {
                        weightedMatrix[i][j] = 20;
                    }
                }

                case 2 ->{
                    // Bad square if you do not have the edge-square to the left, otherwise an okay square.
                    if(pos.isOwnSquare(i, j - 1)){
                        weightedMatrix[i][j] = 3;
                    }
                    else{
                        weightedMatrix[i][j] = -5;
                    }
                }

                case 7 ->{
                    // Bad square if you do not have the edge-square to the right, otherwise an okay square.
                    if(pos.isOwnSquare(i, j + 1)){
                        weightedMatrix[i][j] = 3;
                    }
                    else{
                        weightedMatrix[i][j] = -5;
                    }
                }

                // Good squares because it allows the opponent to take bad squares.
                case 3, 6 -> weightedMatrix[i][j] = 15;

                // Middle square are decent squares.
                default -> weightedMatrix[i][j] = 3;
            }
        }

        else if(i == 6){
            switch (j){
                case 1, 8 ->{
                    // Good square if opponent does not have the corner, because if opponent takes it, it will open up for you to take corner.
                    if(pos.isOpponentSquare(i + 2, j)) {
                        weightedMatrix[i][j] = -5;
                    }
                    else{
                        weightedMatrix[i][j] = 20;
                    }
                }

                case 2 ->{
                    // Bad square if you do not have the edge-square to the left, otherwise an okay square.
                    if(pos.isOwnSquare(i, j - 1)){
                        weightedMatrix[i][j] = 3;
                    }
                    else{
                        weightedMatrix[i][j] = -5;
                    }
                }

                case 7 ->{
                    // Bad square if you do not have the edge-square to the right, otherwise an okay square.
                    if(pos.isOwnSquare(i, j + 1)){
                        weightedMatrix[i][j] = 3;
                    }
                    else{
                        weightedMatrix[i][j] = -5;
                    }
                }

                // Good squares because it allows the opponent to take bad squares.
                case 3, 6 -> weightedMatrix[i][j] = 15;

                // Middle square are decent squares.
                default -> weightedMatrix[i][j] = 3;
            }
        }
    }

    private void matrixInitializeMiddleRows(int i, int j, OthelloPosition pos){
        switch (j){
            // Edge squares are good.
            case 1, 8 -> weightedMatrix[i][j] = 5;

            case 2 ->{
                // Good square if the edge-square to the left is yours, otherwise a bad square.
                if(pos.isOwnSquare(i, j - 1 )){
                    weightedMatrix[i][j] = 3;
                }
                else{
                    weightedMatrix[i][j] = -5;
                }
            }

            case 7 ->{
                // Good square if the edge-square to the right is yours, otherwise a bad square.
                if(pos.isOwnSquare(i, j + 1 )){
                    weightedMatrix[i][j] = 3;
                }
                else{
                    weightedMatrix[i][j] = -5;
                }
            }

            // Rest of the squares are decent squares.
            default -> weightedMatrix[i][j] = 3;
        }
    }

    private void weightedMatrixPrint(){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                System.out.println(i + "," + j + "      " + weightedMatrix[i][j]);
            }
        }
    }
}