/**
 * 
 */

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author tnarayan
 * 
 */
public class Player {
	public static String getMove() {
		return "0 1";
	}

	public static String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static void main(String args[]) throws Exception {

		int width, height, numToWin, playerNumber, timeLimit;

		Scanner input = new Scanner(System.in);

		String move;

		// send player name
		System.out.println("random-player");

		// read game configuration
		String gameConfigString = input.nextLine();
		String[] gameConfig = gameConfigString.split(" ");
		height = Integer.parseInt(gameConfig[0]);
		width = Integer.parseInt(gameConfig[1]);
		numToWin = Integer.parseInt(gameConfig[2]);
		playerNumber = Integer.parseInt(gameConfig[3]);
		timeLimit = Integer.parseInt(gameConfig[4]);

		boolean myTurn = false;

		myTurn = (playerNumber == 1) ? true : false;

		while (true) {
			if (myTurn) {
				// TODO: use a mechanism for timeout(threads, java.util.Timer,
				// ..)

				// call alpha-beta algorithm to get the move
				move = getMove();

				// send move
				System.out.println("0 1");
			} else {
				// read move
				move = input.nextLine();

				String[] move_param = move.split(" ");

				// check for end
				if (Integer.parseInt(move_param[0]) < 0) {
					break;
				}
			}

			// switch turns
			myTurn = !myTurn;
		}
	}
}