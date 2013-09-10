/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package Util;
/**
 * Class to implement the player of the game
 * 
 * @author bli
 * @author tnarayan
 * 
 */
@SuppressWarnings("serial")
public class Connect4Exception extends Exception {

	@SuppressWarnings("unused")
	private final String errorMsg;

	public Connect4Exception(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
