/**
 * 
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author tnarayan
 * 
 */
public class Player {
	public static int getMove() {
		return 1;
	}

	public static void main(String args[]) throws Exception {
		int width, height, numToWin, playerNumber, timeLimit, move;

		// use BufferedReader for easy reading
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));

		// send player name
		System.out.println("random-player");
		System.out.flush();

		// read game configuration
		String[] gameConfig = input.readLine().split(" ");
		height = Integer.parseInt(gameConfig[0]);
		width = Integer.parseInt(gameConfig[1]);
		numToWin = Integer.parseInt(gameConfig[2]);
		playerNumber = Integer.parseInt(gameConfig[3]);
		timeLimit = Integer.parseInt(gameConfig[4]);

		int currentTurn = 1; // first player starts
		int myTurn = (playerNumber == 1) ? 1 : 0;
		while (true) {
			if (currentTurn == myTurn) {
				// TODO: use a mechanism for timeout(threads, java.util.Timer,
				// ..)

				// call alpha-beta algorithm to get the move
				move = getMove();

				// send move
				System.out.println(String.valueOf(move));
				System.out.flush();
			} else {
				// read move
				move = Integer.parseInt(input.readLine());

				// check for end
				if (move < 0)
					break;
			}

			// switch turns
			currentTurn = 1 - currentTurn;
		}
	}
}