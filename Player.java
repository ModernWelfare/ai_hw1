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
	int timeLimit;
	boolean isMax;

	public Player(int playerNumber, int timeLimit, boolean isMax) {
		this.playerNumber = playerNumber;
		this.timeLimit = timeLimit;
		this.isMax = isMax;
	}

	/**
	 * Function to get a valid move
	 * 
	 * @param timeout
	 * @param gameBoard
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		String move = "2 1";
		return move;
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

	/**
	 * Minimax function for deciding the next move
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public int minimax(Board gameBoard, boolean isMax, int playerNum) {
		int valueToReturn;
		int terminalTestValue = gameBoard.isConnectN();
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			valueToReturn = (terminalTestValue == playerNumber) ? 1 : 0;
		} else {
			List<Integer> minimaxResults = new ArrayList<Integer>();
			for (String move : possibleMoves) {
				try {
					int minimaxValue = minimax(
							result(move, gameBoard, 3 - playerNum), !isMax,
							3 - playerNum);
					minimaxResults.add(minimaxValue);
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
			} else {
				// choose the min value
				int minValue = 1;
				for (int value : minimaxResults) {
					if (value < minValue) {
						minValue = value;
					}
				}
				valueToReturn = minValue;
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

	public Board result(String move, Board gameBoard, int playerNum)
			throws Connect4Exception {
		Board newBoard = gameBoard.getDuplicate();
		makeMove(move, playerNum, newBoard);
		newBoard.printBoard();
		return newBoard;
	}
}
