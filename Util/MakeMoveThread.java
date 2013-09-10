/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package Util;
import MoveStrategies.HeuristicStrategy;
import MoveStrategies.MinimaxStrategy;
import MoveStrategies.MoveStrategy;
import Player.Player;

public class MakeMoveThread implements TimedRunnable {
	public Move result;
	public Board gameBoard;
	public Player currentPlayer;

	public MakeMoveThread(Board gameBoard, Player currentPlayer) {
		this.gameBoard = gameBoard;
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void run() {
		MoveStrategy currentStrategy = new MinimaxStrategy(gameBoard,
				currentPlayer);
		result = currentStrategy.proposeMove();
	}

	@Override
	public void expire() {
		if (result == null) {
			MoveStrategy currentStrategy = new HeuristicStrategy(gameBoard,
					currentPlayer);
			result = currentStrategy.proposeMove();
		}
	}
}
