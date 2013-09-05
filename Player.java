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
	boolean isMax;
	String moveToTake;

	List<Board> minGameBoards;
	List<Board> maxGameBoards;

	public Player(int playerNumber, int timeLimit, boolean isMax) {
		this.playerNumber = playerNumber;
		this.opponentNumber = 3 - playerNumber;
		this.timeLimit = timeLimit;
		this.isMax = isMax;
		minGameBoards = new ArrayList<Board>();
		maxGameBoards = new ArrayList<Board>();
	}

	/**
	 * Function to get a valid move
	 * 
	 * @param timeout
	 * @param gameBoard
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		minimax(gameBoard, !isMax, playerNumber);
		return moveToTake;
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

	public Move minMove(Board gameBoard) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Move bestMove = new Move("0 0", 1);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			bestValueOfMove = (terminalTestValue == playerNumber) ? 1 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMax = maxMove(result(move, gameBoard, opponentNumber,
							minGameBoards));
					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove = moveOfMax;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		return bestMove;
	}

	public Move maxMove(Board gameBoard) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", 0);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			bestValueOfMove = (terminalTestValue == playerNumber) ? 1 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMin = minMove(result(move, gameBoard, playerNumber,
							minGameBoards));
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove = moveOfMin;
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
		return maxMove(gameBoard);
		int valueToReturn;
		int terminalTestValue = gameBoard.isConnectN();
		List<String> possibleMoves = getPossibleMoves(gameBoard);
		boolean moveSelected = false;

		// Boards to check for loops in checking to avoid stack overflow

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			valueToReturn = (terminalTestValue == playerNumber) ? 1 : 0;
		} else {
			List<Integer> minimaxResults = new ArrayList<Integer>();
			List<String> suggestedMoves = new ArrayList<String>();
			for (String move : possibleMoves) {
				try {
					int minimaxValue = minimax(
							result(move, gameBoard, playerNum,
									gameBoards.get(playerNum - 1)), !isMax,
							3 - playerNum);
					minimaxResults.add(minimaxValue);
					suggestedMoves.add(move);
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
			if (isMax) {
				// choose the max value
				int maxValue = 0;
				for (int value : minimaxResults) {
					if (value > maxValue) {
						maxValue = value;
					}
				}
				valueToReturn = maxValue;

				for (int i = 0; i < minimaxResults.size(); i++) {
					if (minimaxResults.get(i) == 1) {
						moveToTake = suggestedMoves.get(i);
						moveSelected = true;
					}
				}

				if (!moveSelected && suggestedMoves.size() > 0) {
					moveToTake = suggestedMoves.get(0);
				}
			} else {
				// choose the min value
				int minValue = 1;
				for (int value : minimaxResults) {
					if (value < minValue) {
						minValue = value;
					}
				}
				valueToReturn = minValue;

				for (int i = 0; i < minimaxResults.size(); i++) {
					if (minimaxResults.get(i) == 0) {
						moveToTake = suggestedMoves.get(i);
						moveSelected = true;
					}
				}

				if (!moveSelected && suggestedMoves.size() > 0) {
					moveToTake = suggestedMoves.get(0);
				}
			}
		}
		return valueToReturn;
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

	public Board result(String move, Board gameBoard, int playerNum,
			List<Board> checkedBoards) throws Connect4Exception {
		Board newBoard = gameBoard.getDuplicate();
		makeMove(move, playerNum, newBoard);
		for (Board checkedBoard : checkedBoards) {
			if (checkedBoard.equals(newBoard)) {
				throw new Connect4Exception(
						"Board already checked for the player");
			}
		}
		checkedBoards.add(newBoard);
		return newBoard;
	}
}
