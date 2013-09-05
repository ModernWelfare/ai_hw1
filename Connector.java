/**
 * Class to implement the player of the game
 * 
 * @author bli
 * @author tnarayan
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author bli
 * 
 */
public class Connector {

	public static void main(String args[]) throws IOException {

		int width, height, numToWin, playerNumber, timeLimit;
		boolean myTurn = false;
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));

		// Our own AI player
		Player us;
		// The opponent
		Player adversary;
		// The move to take
		String move;
		// The game board
		Board gameBoard;

		// send player name
		System.out.println("tnarayan_bli");

		// read game configuration
		String gameConfigString = input.readLine();
		String[] gameConfig = gameConfigString.split(" ");
		height = Integer.parseInt(gameConfig[0]);
		width = Integer.parseInt(gameConfig[1]);
		numToWin = Integer.parseInt(gameConfig[2]);
		playerNumber = Integer.parseInt(gameConfig[3]);
		timeLimit = Integer.parseInt(gameConfig[4]);

		// Initialize the board and the player according to the config
		gameBoard = new Board(height, width, numToWin);
		// gameBoard.printBoard();

		myTurn = (playerNumber == 1) ? true : false;

		us = new Player(playerNumber, timeLimit);
		// player number is either 1 or 2
		adversary = new Player(3 - playerNumber, timeLimit);

		while (true) {
			if (myTurn) {
				// TODO: use a mechanism for timeout(threads, java.util.Timer,
				// ..)

				// call alpha-beta algorithm to get the move

				// send move
				move = us.getMove(gameBoard);
				// print the move to be read by the adversary
				try {
					us.makeMove(move, us.playerNumber, gameBoard);
					gameBoard.printBoard();
				} catch (Connect4Exception e) {
					System.out.println("We lost!");
				}
				System.out.println(move);
			} else {
				// read move
				move = input.readLine();

				String[] move_param = move.split(" ");

				// check for end
				if (move_param.length == 1) {
					System.out.println("game over!");
					break;
				} else {
					try {
						adversary.makeMove(move, adversary.playerNumber,
								gameBoard);
						gameBoard.printBoard();
					} catch (Connect4Exception e) {
						System.out.println("opponent lost!");
					}
				}
			}
			// switch turns
			myTurn = !myTurn;
			// gameBoard.printBoard();
		}
	}
}