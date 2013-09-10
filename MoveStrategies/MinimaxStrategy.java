/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */

package MoveStrategies;

import java.util.List;

import Player.Player;
import Util.Board;
import Util.Connect4Exception;
import Util.Move;

/**
 * Implementation class of the minimax strategy
 * 
 * @author Li Bohao
 * 
 */
public class MinimaxStrategy extends MoveStrategyImpl implements MoveStrategy {

	public MinimaxStrategy(Board gameBoard, Player currentPlayer) {
		super(gameBoard, currentPlayer);
		// Auto-generated constructor stub
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
		return maxMove(gameBoard, DEPTH);
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
}
