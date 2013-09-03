/**
 * Class to implement the player of the game
 * 
 * @author Li Bohao
 * 
 */
public class Player {
	int playerNumber;
	int timeLimit;

	public Player(int playerNumber, int timeLimit) {
		this.playerNumber = playerNumber;
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
		return "2 1";
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
	public void makeMove(String moveToTake, Board gameBoard)
			throws Connect4Exception {
		String[] moveArrs = moveToTake.split(" ");
		int col = Integer.parseInt(moveArrs[0]);
		boolean isDrop = (Integer.parseInt(moveArrs[1]) == 1) ? true : false;

		if (isDrop) {
			if (gameBoard.canDropADiscFromTop(col, playerNumber)) {
				gameBoard.dropADiscFromTop(col, playerNumber);
			} else {
				throw new Connect4Exception("Drop: Move illegal");
			}
		} else {
			if (gameBoard.canRemoveADiscFromBottom(col, playerNumber)) {
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
}
