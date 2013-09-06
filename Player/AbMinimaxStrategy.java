package Player;
import java.util.List;

import MoveStrategies.MoveStrategy;
import MoveStrategies.MoveStrategyImpl;
import Util.Board;
import Util.Connect4Exception;
import Util.Move;

/**
 * @author Li Bohao
 * 
 */
public class AbMinimaxStrategy extends MoveStrategyImpl implements MoveStrategy {

	public AbMinimaxStrategy(Board gameBoard, Player currentPlayer) {
		super(gameBoard, currentPlayer);
	}

	@Override
	public Move proposeMove() {
		return abMinimax(gameBoard);
	}

	/**
	 * Minimax function using alpha beta pruning
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move abMinimax(Board gameBoard) {
		return abMaxMove(gameBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Minimax using alpha-beta pruning
	 * 
	 * @param gameBoard
	 * @param stepsLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Move abMinMove(Board gameBoard, int stepsLeft, int alpha, int beta) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Move bestMove = new Move("0 0", Integer.MAX_VALUE);
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
					moveOfMax = abMaxMove(
							result(move, gameBoard, opponentNumber),
							stepsLeft - 1, alpha, beta);
					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMax.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				if (bestMove.moveValue <= alpha) {
					return bestMove;
				}
				beta = Math.min(beta, bestMove.moveValue);
			}
		}
		return bestMove;
	}

	/**
	 * Minimax using alpha-beta pruning
	 * 
	 * @param gameBoard
	 * @param stepsLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Move abMaxMove(Board gameBoard, int stepsLeft, int alpha, int beta) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", Integer.MIN_VALUE);
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
					moveOfMin = abMinMove(
							result(move, gameBoard, playerNumber),
							stepsLeft - 1, alpha, beta);
					// System.out.println(moveOfMin.moveValue);
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMin.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				if (bestMove.moveValue >= beta) {
					return bestMove;
				}
				alpha = Math.max(bestMove.moveValue, alpha);
			}
		}
		// gameBoard.printBoard();
		// System.out.println("Bestmove" + bestMove.moveValue);
		// System.out.println("Bestmove" + bestMove.moveString);
		return bestMove;
	}
}
