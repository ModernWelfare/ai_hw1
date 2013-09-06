package MoveStrategies;

import java.util.ArrayList;
import java.util.List;

import Player.Player;
import Util.Board;
import Util.Connect4Exception;
import Util.Move;

/**
 * Abstract class for move strategies
 * 
 * @author Li Bohao
 * 
 */
public abstract class MoveStrategyImpl implements MoveStrategy {
	protected Board gameBoard;
	protected Player currentPlayer;
	protected int playerNumber;
	protected int opponentNumber;
	protected int DEPTH;

	public MoveStrategyImpl(Board gameBoard, Player currentPlayer) {
		this.gameBoard = gameBoard;
		this.currentPlayer = currentPlayer;
		this.playerNumber = currentPlayer.playerNumber;
		this.opponentNumber = currentPlayer.opponentNumber;
		DEPTH = 6;
	}

	@Override
	public Move proposeMove() {
		return null;
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
