import java.util.ArrayList;
import java.util.List;

/**
 * Class to implement the player of the game
 * 
 * @author bli
 * 
 */
public class Player {
	int playerNumber;
	int opponentNumber;
	int timeLimit;

	// List<Board> minGameBoards;
	// List<Board> maxGameBoards;

	public Player(int playerNumber, int timeLimit) {
		this.playerNumber = playerNumber;
		this.opponentNumber = 3 - playerNumber;
		this.timeLimit = timeLimit;
	}

	/**
	 * Function to get a valid move
	 * 
	 * @param timeout
	 * @param gameBoard
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		Move moveToTake = minimax(gameBoard);
		return moveToTake.moveString;
	}

	/**
	 * Function for the player to make a move based on a string
	 * 
	 * @param moveToTake
	 *            The move the player is going to make
	 * @param gameBoard
	 *            The board on which the player makes the move on
	 * @throws Connect4Exception
	 *             Throw exception if the move isn't legal
	 */
	public void makeMove(String moveToTake, int playerNum, Board gameBoard)
			throws Connect4Exception {
		String[] moveArrs = moveToTake.split(" ");
		int col = Integer.parseInt(moveArrs[0]);
		boolean isDrop = (Integer.parseInt(moveArrs[1]) == 1) ? true : false;

		if (isDrop) {
			if (gameBoard.canDropADiscFromTop(col, playerNum)) {
				gameBoard.dropADiscFromTop(col, playerNum);
			} else {
				throw new Connect4Exception("Drop: Move illegal");
			}
		} else {
			if (gameBoard.canRemoveADiscFromBottom(col, playerNum)) {
				gameBoard.removeADiscFromBottom(col);
			} else {
				throw new Connect4Exception("Remove: Move illegal");
			}
		}
	}

	/**
	 * Function to print the information of the player
	 */
	public void printInfo() {
		System.out.println(playerNumber + ": " + timeLimit + "\n");
	}

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
		// gameBoard.printBoard();
		// System.out.println("Bestmove" + bestMove.moveValue);
		// System.out.println("Bestmove" + bestMove.moveString);
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
		return maxMove(gameBoard, gameBoard.width * gameBoard.height / 2);
	}

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
		makeMove(move, playerNum, newBoard);
		// for (Board checkedBoard : checkedBoards) {
		// if (checkedBoard.equals(newBoard)) {
		// throw new Connect4Exception(
		// "Board already checked for the player");
		// }
		// }
		// checkedBoards.add(newBoard);
		return newBoard;
	}
}
