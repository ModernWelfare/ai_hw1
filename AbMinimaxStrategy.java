import java.util.ArrayList;
import java.util.List;

/**
 * @author Li Bohao
 * 
 */
public class AbMinimaxStrategy implements MoveStrategy {
	public Board gameBoard;
	public Player currentPlayer;
	int playerNumber;
	int opponentNumber;
	int DEPTH;

	public AbMinimaxStrategy(Board gameBoard, Player currentPlayer) {
		this.gameBoard = gameBoard;
		this.currentPlayer = currentPlayer;
		this.playerNumber = currentPlayer.playerNumber;
		this.opponentNumber = currentPlayer.opponentNumber;
		DEPTH = 6;
	}

	@Override
	public Move proposeMove() {
		return abMinimax(gameBoard);
	}

	/**
	 * Minimax using alpha-beta pruning
	 * 
	 * @param gameBoard
	 * @param stepsLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Move abMinMove(Board gameBoard, int stepsLeft, int alpha, int beta) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Move bestMove = new Move("0 0", Integer.MAX_VALUE);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else if (stepsLeft == 0) {
			bestMove = new Move("0 0", 1);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMax = abMaxMove(
							result(move, gameBoard, opponentNumber),
							stepsLeft - 1, alpha, beta);
					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMax.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				if (bestMove.moveValue <= alpha) {
					return bestMove;
				}
				beta = Math.min(beta, bestMove.moveValue);
			}
		}
		return bestMove;
	}

	/**
	 * Minimax using alpha-beta pruning
	 * 
	 * @param gameBoard
	 * @param stepsLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Move abMaxMove(Board gameBoard, int stepsLeft, int alpha, int beta) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", Integer.MIN_VALUE);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			// System.out.println(terminalTestValue);
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else if (stepsLeft == 0) {
			bestMove = new Move("0 0", 1);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMin = abMinMove(
							result(move, gameBoard, playerNumber),
							stepsLeft - 1, alpha, beta);
					// System.out.println(moveOfMin.moveValue);
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMin.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				if (bestMove.moveValue >= beta) {
					return bestMove;
				}
				alpha = Math.max(bestMove.moveValue, alpha);
			}
		}
		// gameBoard.printBoard();
		// System.out.println("Bestmove" + bestMove.moveValue);
		// System.out.println("Bestmove" + bestMove.moveString);
		return bestMove;
	}

	/**
	 * Minimax function using alpha beta pruning
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move abMinimax(Board gameBoard) {
		return abMaxMove(gameBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * returns a list of possible moves on the board note that not all of these
	 * moves are legal!
	 */
	public List<String> getPossibleMoves(Board gameBoard) {
		int width = gameBoard.width;
		List<String> possibleMoves = new ArrayList<String>();

		String possibleDrop;
		String possiblePop;
		for (int i = 0; i < width; i++) {
			possibleDrop = Integer.toString(i).concat(" 1");
			possibleMoves.add(possibleDrop);
			possiblePop = Integer.toString(i).concat(" 0");
			possibleMoves.add(possiblePop);
		}
		return possibleMoves;
	}

	public Board result(String move, Board gameBoard, int playerNum)
			throws Connect4Exception {
		Board newBoard = gameBoard.getDuplicate();
		currentPlayer.makeMove(move, playerNum, newBoard);
		return newBoard;
	}

}
