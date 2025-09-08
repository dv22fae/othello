import java.util.LinkedList;

/**
 * This is where you implement the alpha-beta algorithm.
 * See <code>OthelloAlgorithm</code> for details
 * 
 * @author Henrik Bj&ouml;rklund
 *
 */
public class AlphaBeta implements OthelloAlgorithm {
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
		// Iterativt söka ALFABETA algoritmen med olika djup. Så verkar det som vi ska göra enligt uppgiften.
		for(int i = 1 ; i <= searchDepth; i++){
			// Spara bästa action,
			// Mät tid och när tiden tar slut så returnerar vi bästa action.
			minMax(bla,bla,bla)
		}
		// TODO: implement the alpha-beta algorithm
	}

	private int minMax(OthelloPosition pos, int depth, boolean whitePlayer) {
		/* Algorithm:
		Man söker djupt hela vägen ner till löv. När vit spelar väljer den det drag som ger
		mest antal vita brickor (MAX). När svart spelar väljer den det drag som ger minst antal vita
		brickor (MIN). Så man har ett träd med alla drag och varannan nivå är svarts frag och varannan vits drag.
		Man sparar alltså undan antalet brickor och väljer det bästa draget och sparar vägen till den noden.
		Så att man kan skapa en OthelloAction med x och y värde osv osv. Denna algorithm är baserad på att
		vit och svart båda letar på samma sätt efter det mest gynnsamma drag för dem själva.
		 */

		// Evaluera positionen längst ner i träder (löven).
		if (depth == 0) {
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
}