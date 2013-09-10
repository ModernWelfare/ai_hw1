/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package Player;
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

import Util.Board;
import Util.Connect4Exception;

/**
 * @author bli
 * @author tnarayan
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
		String gameConfigString = input.readLine(); //referee outputs player names to us, read and discard
		gameConfigString = input.readLine(); //read game configuration
		String[] gameConfig = gameConfigString.split(" ");
		height = Integer.parseInt(gameConfig[0]);
		width = Integer.parseInt(gameConfig[1]);
		numToWin = Integer.parseInt(gameConfig[2]);
		playerNumber = Integer.parseInt(gameConfig[3]);
		timeLimit = Integer.parseInt(gameConfig[4]);

		// Initialize the board and the player according to the config
		gameBoard = new Board(height, width, numToWin);

		myTurn = (playerNumber == 1) ? true : false;

		us = new Player(playerNumber, timeLimit);
		// player number is either 1 or 2
		adversary = new Player(3 - playerNumber, timeLimit);

		while (true) {
			if (myTurn) {
				// call alpha-beta algorithm to get the move
				// send move
				move = us.getMove(gameBoard);
				// print the move to be read by the adversary
				try {
					us.makeMove(move, us.playerNumber, gameBoard);
				} catch (Connect4Exception e) {
				}
				System.out.println(move);
			} else {
				// read move
				move = input.readLine();

				String[] move_param = move.split(" ");

				// check for end
				if (move_param.length == 1) { //game over
					break;
				} else {
					try {
						adversary.makeMove(move, adversary.playerNumber,
								gameBoard);
					} catch (Connect4Exception e) { //opponent lost
					}
				}
			}
			// switch turns
			myTurn = !myTurn;
		}
	}
}