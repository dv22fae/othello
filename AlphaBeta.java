import java.util.LinkedList;
import java.util.Timer;

/**
 * This is where you implement the alpha-beta algorithm.
 * See <code>OthelloAlgorithm</code> for details
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
		// Hämta alla barn "t" i trädet till "s"
		LinkedList <OthelloAction> bestMove = pos.getMoves();

		if (bestMove.isEmpty()) {
			// Movet är inte valid, alltså pass.
			return new OthelloAction(0, 0, true);
		}


		// Vems tur är det? Sant om vit (MAX) ska spela, falskt om svart (MIN)
		boolean whitesMove = pos.toMove();

		// Beta är plus oändligheten och alpha är tvärtom.
		// α = -∞
		int alpha = NEG_INFINITY;
		// β = +∞
		int beta  = POS_INFINITY;

		// Lagrar bästa draget hittils.
		OthelloAction best = null;

		// MAX startar från -∞, MIN från +∞ (value = -∞ eller +∞).
		int bestValue;
		if (whitesMove) {
			bestValue = NEG_INFINITY;
		} else {
			bestValue = POS_INFINITY;
		}

		// för varje barn t av gör följande
		for (OthelloAction move : bestMove) {
			// Iterativt söka ALFABETA algoritmen med olika djup. Så verkar det som vi ska göra enligt uppgiften.
			// bla bla bla

			// Skapa en kopia av s.
			OthelloPosition child = pos.clone();

			// vet inte än om det ska göra här eller inte.
			child = child.makeMove(move);

			int value = minMax(child, searchDepth, alpha, beta, !whitesMove);

			if (whitesMove){
				// gör det här träd jobbet, den större än den osv.
				alpha = Math.max(alpha, bestValue);
			}
			else{
				beta = Math.min(beta, bestValue);
			}
		}

		if (best == null) {
			return new OthelloAction(0, 0, true);
		}

		best.setValue(bestValue);
		return best;





		//for(int i = 1 ; i <= searchDepth; i++){
			// Spara bästa action,
			// Mät tid och när tiden tar slut så returnerar vi bästa action.
			// minMax(bla,bla,bla)
		//}
		// TODO: implement the alpha-beta algorithm
	}


	/* Algorithm:
Man söker djupt hela vägen ner till löv. När vit spelar väljer den det drag som ger
mest antal vita brickor (MAX). När svart spelar väljer den det drag som ger minst antal vita
brickor (MIN). Så man har ett träd med alla drag och varannan nivå är svarts frag och varannan vits drag.
Man sparar alltså undan antalet brickor och väljer det bästa draget och sparar vägen till den noden.
Så att man kan skapa en OthelloAction med x och y värde osv osv. Denna algorithm är baserad på att
vit och svart båda letar på samma sätt efter det mest gynnsamma drag för dem själva.
 */
	/*
	private int minMax(OthelloPosition pos, int depth, boolean whitePlayer) {


		// Evaluera positionen längst ner i träder (löven).
		if (depth == 0) {
			return evaluator.evaluate(pos);
		}

		LinkedList<OthelloAction> moves = pos.getMoves();
		if (moves.isEmpty()) {
			// Movet är inte valid.
			return evaluator.evaluate(pos);
		}


		// Om det är vits tur att spela.
		if (whitePlayer) {
			int maxWhite = -Integer.MAX_VALUE;
			LinkedList<OthelloAction> possibleActions = pos.getMoves();

			// Om vi inte kan göra några moves.
			if (possibleActions.isEmpty()) {
				// Då ska vi skapa en pass action?
				return evaluator.evaluate(pos);
			}

			// Skapar en kopia så att när vi ska lista ut vilken action vi ska ta så gör vi inte den action på riktiga boardet.
			OthelloPosition copy = pos.clone();
			// För alla möjliga actions man kan ta från nuvarande stadiet (positionen).
			for (OthelloAction action : possibleActions) {
				// Måste skapa en ny position (nytt state) genom childPos.makeMove som uppdaterar spelplanen med en action?
				// Det är vi som måste implementera denna metod.
				// Vi gör en action på kopian som resulterar i en ny uppdaterad posistion som vi sedan kör minmax på igen.
				copy = copy.makeMove(action);

				int currentWhite = minMax(copy, depth - 1, false);

				if (currentWhite > maxWhite) {
					maxWhite = currentWhite;
				}
			}
			return maxWhite;
		}

		else {
			int minWhite = Integer.MAX_VALUE;
			LinkedList<OthelloAction> possibleActions = pos.getMoves();

			// Om vi inte kan göra några moves.
			if (possibleActions.isEmpty()) {
				// Då ska vi skapa en pass action?
				return evaluator.evaluate(pos);
			}

			// Skapar en kopia så att när vi ska lista ut vilken action vi ska ta så gör vi inte den action på riktiga boardet.
			OthelloPosition copy = pos.clone();

			// För alla möjliga actions man kan ta från nuvarande stadiet (positionen).
			for (OthelloAction action : possibleActions) {
				// Måste skapa en ny position (nytt state) genom childPos.makeMove som uppdaterar spelplanen med en action?
				// Det är vi som måste implementera denna metod.
				copy = copy.makeMove(action);

				int currentMinWhite = minMax(copy, depth - 1, true);

				if (currentMinWhite < minWhite) {
					minWhite = currentMinWhite;
				}
			}
			return minWhite;
		}
	}
	*/
	public OthelloAction evaluate(OthelloPosition pos) {
		OthelloAction bestAction = null;

		for(int i = 1; i < searchDepth; i++){
			maxValue(pos, NEG_INFINITY, POS_INFINITY, i, bestAction);
		}
	}

	// NY
	private int maxValue(OthelloPosition pos, int alpha, int beta, int depth, OthelloAction bestAction){
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop at the bottom of the tree or if no possible move is available.
		if(depth == 0 || possibleActions.isEmpty()){
			return evaluator.evaluate(pos);
		}

		int maxVal = NEG_INFINITY;

		// For each possible action.
		for(OthelloAction action : possibleActions){

			// Makes a move on the copy so that we get an updated position (state)
			OthelloPosition copiedPos = pos.clone();
			copiedPos = copiedPos.makeMove(action);

			// Saves the biggest value from maxVal and the result from minVal().
			maxVal = Math.max(maxVal, minValue(copiedPos, alpha, beta, depth - 1));

			// Updates alfa.
			alpha = Math.max(alpha, maxVal);

			// If alfa is greater or equal to beta we can prune.
			if(alpha >= beta){
				break;
			}
		}
		return maxVal;
	}

	// NY
	private int minValue(OthelloPosition pos, int alpha, int beta, int depth, OthelloAction bestAction){
		LinkedList<OthelloAction> possibleActions = pos.getMoves();

		// We stop and evaluate at the bottom of the tree or if no possible move is available.
		if(depth == 0 || possibleActions.isEmpty()){
			return evaluator.evaluate(pos);
		}

		int minVal = POS_INFINITY;

		// For each possible action.
		for(OthelloAction action : possibleActions){

			// Makes a move on the copy so that we get an updated position (state)
			OthelloPosition copiedPos = pos.clone();
			copiedPos = copiedPos.makeMove(action);

			// Saves the smallest value from minVal and the result from maxVal().
			minVal = Math.min(minVal, maxValue(copiedPos, alpha, beta, depth - 1));

			// Updates beta.
			beta = Math.min(beta, minVal);

			// If alfa is greater or equal to beta we can prune.
			if(alpha >= beta){
				break;
			}
		}
		return minVal;
	}

}