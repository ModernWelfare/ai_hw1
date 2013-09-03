import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class RandomPlayer {

	String playerName = "tnarayan_bli";
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	public boolean myTurn = false;
	public int height, width, numToWin, firstPlayerNumber, timeLimit,
			ourPlayerNumber;
	public String move;
	public Player us, adversary;
	public Board gameBoard;

	public void processInput() throws IOException, Connect4Exception {

		String s = input.readLine();
		List<String> ls = Arrays.asList(s.split(" "));
		if (ls.size() == 2) {
			try {
				adversary.makeMove(s, gameBoard);
			} catch (Connect4Exception e) {
				System.out.println("opponent lost!");
			}

			move = us.getMove(gameBoard);
			// print the move to be read by the adversary
			try {
				us.makeMove(move, gameBoard);
			} catch (Connect4Exception e) {
				System.out.println(move);
				throw new Connect4Exception("We Lost!!!");
			}
			System.out.println(move);
		} else if (ls.size() == 1) {
			throw new Connect4Exception("game over!!!");
		} else if (ls.size() == 5) {
			// ls contains game info
			height = Integer.parseInt(ls.get(0));
			width = Integer.parseInt(ls.get(1));
			numToWin = Integer.parseInt(ls.get(2));
			firstPlayerNumber = Integer.parseInt(ls.get(3));
			timeLimit = Integer.parseInt(ls.get(4));

			// Initialize the board and the player according to the config
			gameBoard = new Board(height, width, numToWin);
			// gameBoard.printBoard();

			boolean isMax = (ourPlayerNumber == firstPlayerNumber);

			us = new Player(ourPlayerNumber, timeLimit, isMax);
			// player number is either 1 or 2
			adversary = new Player(3 - ourPlayerNumber, timeLimit, !isMax);

			if (isMax) {
				// send move
				move = us.getMove(gameBoard);
				// print the move to be read by the adversary
				try {
					us.makeMove(move, gameBoard);
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				System.out.println(move);
			}
		} else if (ls.size() == 4) { // player1: aa player2: bb
			if ((ls.get(1).equals(playerName))) {
				ourPlayerNumber = 1;
			} else {
				ourPlayerNumber = 2;
			}
		} else {
			System.out.println("not what I want");
		}
	}

	public static void main(String[] args) throws IOException {
		RandomPlayer rp = new RandomPlayer();
		System.out.println(rp.playerName);

		while (true) {
			try {
				rp.processInput();
			} catch (Connect4Exception e) {
				// Just admit defeat
				e.getMessage();
			}
		}
	}

}