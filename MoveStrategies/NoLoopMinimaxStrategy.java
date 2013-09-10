/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package MoveStrategies;

import java.util.ArrayList;
import java.util.List;

import Player.Player;
import Util.Board;
import Util.Connect4Exception;
import Util.Move;

public class NoLoopMinimaxStrategy extends AbMinimaxStrategy implements
		MoveStrategy {

	private final List<Board> maxCheckedPath;
	private final List<Board> minCheckedPath;

	public NoLoopMinimaxStrategy(Board gameBoard, Player currentPlayer) {
		super(gameBoard, currentPlayer);
		maxCheckedPath = new ArrayList<Board>();
		minCheckedPath = new ArrayList<Board>();
	}

	@Override
	public Move proposeMove() {
		return minimax(gameBoard);
	}

	/**
	 * Minimax function for deciding the next move
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move minimax(Board gameBoard) {
		maxCheckedPath.add(gameBoard);
		return maxMove(gameBoard);
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
	public Move minMove(Board gameBoard) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Board resultBoard;
		Move bestMove = new Move("0 0", 2);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else {
			for (String move : possibleMoves) {
				try {
					resultBoard = result(move, gameBoard, opponentNumber);

					if (boardChecked(maxCheckedPath, resultBoard)) {
						moveOfMax = new Move(move, 1);
					} else {
						maxCheckedPath.add(resultBoard);
						moveOfMax = maxMove(resultBoard);
					}

					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMax.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		minCheckedPath.remove(minCheckedPath.size() - 1);
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
	public Move maxMove(Board gameBoard) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", 0);
		Board resultBoard;
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			// System.out.println(terminalTestValue);
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else {
			for (String move : possibleMoves) {
				try {
					resultBoard = result(move, gameBoard, playerNumber);

					if (boardChecked(minCheckedPath, resultBoard)) {
						moveOfMin = new Move(move, 1);
					} else {
						minCheckedPath.add(resultBoard);
						moveOfMin = minMove(resultBoard);
					}

					// selects the move with the maximum move value
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMin.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		maxCheckedPath.remove(maxCheckedPath.size() - 1);
		return bestMove;
	}

	private boolean boardChecked(List<Board> checkedPath, Board boardToCheck) {
		for (Board pastBoard : checkedPath) {
			if (pastBoard.equals(boardToCheck)) {
				return true;
			}
		}
		return false;
	}

}
