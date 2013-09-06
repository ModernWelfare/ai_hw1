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
		result = currentPlayer.abMinimax(gameBoard);
	}

	@Override
	public void expire() {
		if (result == null) {
			result = new Move("9 9", 99);
		}
	}
}
