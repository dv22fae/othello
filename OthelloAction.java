
/**
 * This class represents a 'move' in a game. The move is simply represented by
 * two integers: the row and the column where the player puts the marker. In
 * addition, the <code>OthelloAction</code> has a field where the estimated
 * value of the move can be stored during computations.
 *
 * @author Henrik Björklund, original
 * @author Fredrik Alexandre, co-author
 * @author Samuel Hagner, co-author
 *
 * @version 2.0, 2025-09-24
 */

public class OthelloAction {

    /**
     * The row where the marker is placed.
     */
    protected int row = -1;

    /**
     * The column where the marker is placed.
     */
    protected int column = -1;

    /**
     * The estimated value of the move.
     */
    protected int value = 0;

    /**
     * True if the player has to pass, i.e., if there is no legal move.
     */
    protected boolean pass = false;

    /**
     * Creates a new <code>OthelloAction</code> with row <code>r</code>, column
     * <code>c</code>, and value 0.
     *
     * @param r, row.
     * @param c, column.
     */
    public OthelloAction(int r, int c) {
        row = r;
        column = c;
        value = 0;
    }

    /**
     * Creates a move with the given row/column and pass flag.
     *
     * @param r, row.
     * @param c, column.
     * @param p true for a pass move, otherwise false.
     */
    public OthelloAction(int r, int c, boolean p) {
        row = r;
        column = c;
        value = 0;
        pass = p;
    }

    /**
     * Creates a move from a string.
     *
     * @param s, string to parse.
     */
    public OthelloAction(String s) {
        if (s.equals("pass")) {
            row = 0;
            column = 0;
            value = 0;
            pass = true;
        } else {
            row = Character.getNumericValue(s.charAt(1));
            column = Character.getNumericValue(s.charAt(3));
            value = 0;
        }
    }

    /**
     * Sets the estimated value of the move.
     *
     * @param v, value to store.
     */
    public void setValue(int v) {
        value = v;
    }

    /**
     * Returns the estimated value of the move.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the column where the marker is to be placed.
     *
     * @param c, column.
     */
    public void setColumn(int c) {
        column = c;
    }

    /**
     * Returns the column where the marker is to be placed.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the row where the marker is to be placed.
     *
     * @param r, row.
     */
    public void setRow(int r) {
        row = r;
    }

    /**
     * Returns the row where the marker is to be placed.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the boolean that indicates whether this is a pass move. This should only
     * be true if there are no legal moves.
     *
     * @param b, true if it is a pass move, otherwise false.
     */
    public void setPassMove(boolean b) {
        pass = b;
    }

    /**
     * Returns true if this is a pass move, indicating that the player has no legal
     * moves. O therwise returns false.
     */
    public boolean isPassMove() {
        return pass;
    }

    /**
     * Prints this action to standard output.
     */
    public void print() {
        if (pass) {
            System.out.println("pass");
        } else {
            System.out.println("(" + row + "," + column + ")");
        }
    }

}
