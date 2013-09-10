/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package Player;

import MoveStrategies.MoveStrategy;
import Util.Board;
import Util.Connect4Exception;
import Util.Move;

/**
 * Class to implement the player of the game
 * 
 * @author bli
 * @author tnarayan
 * 
 */
public class MockPlayer extends Player {
	public int playerNumber;
	public int opponentNumber;
	MoveStrategy testStrategy;
	int timeLimit;
	int DEPTH;

	public MockPlayer(int playerNumber, int timeLimit) {
		super(playerNumber, timeLimit);
		// Auto-generated constructor stub
	}

	public void setStrategy(MoveStrategy testStrategy) {
		this.testStrategy = testStrategy;
	}

	/**
	 * Function to get a valid move. It uses a limited amount of time to execute
	 * the minimax function
	 * 
	 * @param gameBoard
	 *            the board on which the game is carried out
	 * @return a string representing the move
	 */
	@Override
	public String getMove(Board gameBoard) {
		Move moveToTake = testStrategy.proposeMove();
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
	@Override
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
	@Override
	public void printInfo() {
		System.out.println(playerNumber + ": " + timeLimit + "\n");
	}

}
