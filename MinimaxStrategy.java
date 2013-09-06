import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class of the minimax strategy
 * 
 * @author Li Bohao
 * 
 */
public class MinimaxStrategy implements MoveStrategy {

	public Board gameBoard;
	public Player currentPlayer;
	int playerNumber;
	int opponentNumber;
	int DEPTH;

	public MinimaxStrategy(Board gameBoard, Player currentPlayer) {
		this.gameBoard = gameBoard;
		this.currentPlayer = currentPlayer;
		this.playerNumber = currentPlayer.playerNumber;
		this.opponentNumber = currentPlayer.opponentNumber;
		DEPTH = 6;
	}

	@Override
	public Move proposeMove() {
		return minimax(gameBoard);
	}

	/**
	 * Function for min to propose a move given the board
	 * 
	 * @param gameBoard
	 *            the board on which the game is played
	 * @param stepsLeft
	 *            the depth of the search, when stepsLeft reaches 0, the
	 *            function returns a possibly invalid move with a middle value
	 * @return the proposed move of min
	 */
	public Move minMove(Board gameBoard, int stepsLeft) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Move bestMove = new Move("0 0", 2);
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
					moveOfMax = maxMove(
							result(move, gameBoard, opponentNumber),
							stepsLeft - 1);
					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMax.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		return bestMove;
	}

	/**
	 * Function for max to propose a move given the board
	 * 
	 * @param gameBoard
	 *            the board on which the game is played
	 * @param stepsLeft
	 *            the depth of the search, when stepsLeft reaches 0, the
	 *            function returns a possibly invalid move with a middle value
	 * @return the proposed move of max
	 */
	public Move maxMove(Board gameBoard, int stepsLeft) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", 0);
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
					moveOfMin = minMove(result(move, gameBoard, playerNumber),
							stepsLeft - 1);
					// System.out.println(moveOfMin.moveValue);
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMin.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		return bestMove;
	}

	/**
	 * Minimax function for deciding the next move
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move minimax(Board gameBoard) {
		return maxMove(gameBoard, DEPTH);
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
