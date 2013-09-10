/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */

package Player;

import Util.Board;
import Util.Connect4Exception;
import Util.MakeMoveThread;
import Util.Move;
import Util.TimedTaskExecuter;

/**
 * Class to implement the player of the game
 * 
 * @author bli
 * @author tnarayan
 * 
 */
public class Player {
	public int playerNumber;
	public int opponentNumber;
	int timeLimit;
	int DEPTH;

	// List<Board> minGameBoards;
	// List<Board> maxGameBoards;

	/**
	 * Constructor for the player
	 * 
	 * @param playerNumber
	 *            The number of the player, either 1 or 2
	 * @param timeLimit
	 *            The time limit given to the player to make the move
	 */
	public Player(int playerNumber, int timeLimit) {
		this.playerNumber = playerNumber;
		this.opponentNumber = 3 - playerNumber;
		this.timeLimit = timeLimit;
		this.DEPTH = 6;
	}

	/**
	 * Function to get a valid move. It uses a limited amount of time to execute
	 * the minimax function
	 * 
	 * @param gameBoard
	 *            the board on which the game is carried out
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		Move moveToTake = new Move("0 0", 999);
		MakeMoveThread mmt = new MakeMoveThread(gameBoard, new Player(
				playerNumber, timeLimit));
		TimedTaskExecuter.execute(mmt, (timeLimit - 2) * 1000);
		try {
			Thread.sleep((timeLimit - 2) * 1000 + 500);
		} catch (InterruptedException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		moveToTake = mmt.result;
		return moveToTake.moveString;
	}

	/**
	 * Function for the player to make a move based on a string.
	 * 
	 * @param moveToTake
	 *            The move the player is going to make
	 * @param playerNum
	 *            represents the current player
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
		} else { // the move made was to remove a disc
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

}
