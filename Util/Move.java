/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package Util;

/**
 * @author Li Bohao
 * @author tnarayan
 * 
 */
public class Move {
	public String moveString;
	public int moveValue;

	// empty constructor
	public Move() {
		this.moveString = "";
		this.moveValue = 0;
	}

	// constructor with params
	public Move(String moveString, int moveValue) {
		this.moveString = moveString;
		this.moveValue = moveValue;
	}

	// getters
	public String getMoveString() {
		return this.moveString;
	}

	public void setMoveString(String moveString) {
		this.moveString = moveString;
	}

	// setters
	public int getMoveValue() {
		return this.moveValue;
	}

	public void setMoveValue(int moveValue) {
		this.moveValue = moveValue;
	}
}
