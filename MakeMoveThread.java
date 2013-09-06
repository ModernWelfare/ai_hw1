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
		MoveStrategy currentStrategy = new AbMinimaxStrategy(gameBoard,
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
