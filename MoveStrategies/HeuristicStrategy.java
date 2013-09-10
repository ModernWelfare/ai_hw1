/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */

package MoveStrategies;

import java.util.ArrayList;
import java.util.List;

import Player.Player;
import Util.Board;
import Util.Connect4Exception;
import Util.Move;

/**
 * Implementation of the heuristic strategies
 * 
 * @author tnarayan
 * 
 */
public class HeuristicStrategy implements MoveStrategy {
	public Board gameBoard;
	public Player currentPlayer;
	int playerNumber;
	int opponentNumber;

	public HeuristicStrategy(Board gameBoard, Player currentPlayer) {
		this.gameBoard = gameBoard;
		this.currentPlayer = currentPlayer;
		this.playerNumber = currentPlayer.playerNumber;
		this.opponentNumber = currentPlayer.opponentNumber;
	}

	@Override
	public Move proposeMove() {
		return bestMoveToMake(gameBoard, gameBoard.N,
				currentPlayer.playerNumber);
	}

	/**
	 * returns a list of possible moves on the board. note that not all of these
	 * moves are legal!
	 */
	public List<String> getPossibleMoves(Board gameBoard) {
		int width = gameBoard.width;
		List<String> possibleMoves = new ArrayList<String>();

		String possibleDrop;
		String possiblePop;
		for (int i = 0; i < width; i++) {
			possibleDrop = Integer.toString(i).concat(" 1");
			possibleMoves.add(possibleDrop);
			possiblePop = Integer.toString(i).concat(" 0");
			possibleMoves.add(possiblePop);
		}
		return possibleMoves;
	}

	public Board result(String move, Board gameBoard, int playerNum)
			throws Connect4Exception {
		Board newBoard = gameBoard.getDuplicate();
		currentPlayer.makeMove(move, playerNum, newBoard);
		return newBoard;
	}

	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found vertically, 0 otherwise
	 */
	public int evaluateTokensInARowVertically(int numTokens, Board gameBoard,
			int numRow, int numColumn) {
		int countOfTokensEncounteredVertically = 0;

		for (int i = numRow; i < gameBoard.height; i++) {
			if (gameBoard.board[numRow][numColumn] == gameBoard.board[i][numColumn]) {
				countOfTokensEncounteredVertically++;
			} else {
				// since the continuity is broken, stop
				break;
			}
		}

		if (countOfTokensEncounteredVertically >= numTokens) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found horizontally, 0 otherwise
	 */
	public int evaluateTokensInARowHorizontally(int numTokens, Board gameBoard,
			int numRow, int numColumn) {
		int countOfTokensEncounteredHorizontally = 0;

		for (int i = numColumn; i < gameBoard.width; i++) {
			if (gameBoard.board[numRow][numColumn] == gameBoard.board[numRow][i]) {
				countOfTokensEncounteredHorizontally++;
			} else {
				// since the continuity is broken, stop
				break;
			}
		}

		if (countOfTokensEncounteredHorizontally >= numTokens) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found in the left diagonal, 0
	 *         otherwise
	 */
	public int evaluateTokensInARowDiagonallyLeft(int numTokens,
			Board gameBoard, int numRow, int numColumn) {
		int countOfTokensEncounteredDiagonallyLeft = 0;

		int j = numColumn;

		for (int i = numRow; i > -1; i--, j++) {
			if (j > gameBoard.height) { // check to make sure not out of bounds
				break;
			} else if (gameBoard.board[numRow][numColumn] == gameBoard.board[i][j]) {
				countOfTokensEncounteredDiagonallyLeft++;
			} else {
				break;
			}
		}

		if (countOfTokensEncounteredDiagonallyLeft >= numTokens) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found in the right diagonal, 0
	 *         otherwise
	 */
	public int evaluateTokensInARowDiagonallyRight(int numTokens,
			Board gameBoard, int numRow, int numColumn) {
		int countOfTokensEncounteredDiagonallyRight = 0;

		int j = numColumn;

		for (int i = numRow; i < gameBoard.height; i++, j++) {
			if (j > gameBoard.height) { // make sure j is not out of bounds
				break;
			} else if (gameBoard.board[numRow][numColumn] == gameBoard.board[i][j]) {
				countOfTokensEncounteredDiagonallyRight++;
			} else {
				break;
			}
		}

		if (countOfTokensEncounteredDiagonallyRight >= numTokens) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param numTokens
	 *            the N-in-a-row number of tokens being searched for
	 * @param playerNum
	 *            the player whose tokens are being searched
	 * @param gameBoard
	 * 
	 * @return the number of tokens encountered for the specified N-in-a-row
	 */
	public int evaluateTokensInARow(int numTokens, int playerNum,
			Board gameBoard) {
		int countOfTokensEncountered = 0;

		for (int i = 0; i < gameBoard.height; i++) {
			for (int j = 0; j < gameBoard.width; j++) {
				if (gameBoard.board[i][j] == playerNum) {// check only our
															// tokens;
					// ignore the opponent's
					// tokens
					countOfTokensEncountered += evaluateTokensInARowVertically(
							numTokens, gameBoard, i, j);
					countOfTokensEncountered += evaluateTokensInARowHorizontally(
							numTokens, gameBoard, i, j);
					countOfTokensEncountered += evaluateTokensInARowDiagonallyLeft(
							numTokens, gameBoard, i, j);
					countOfTokensEncountered += evaluateTokensInARowDiagonallyRight(
							numTokens, gameBoard, i, j);
				}
			}
		}

		return countOfTokensEncountered;
	}

	/**
	 * Evaluates the relative preferability of the board: fours-in-a-row are
	 * weighted more than threes-in-a-row, which in turn are weighted more than
	 * twos-in-a-row
	 * 
	 * Extending this to connect-N: twos-in-a-row, threes-in-a-row, ...,
	 * n-in-a-row
	 * 
	 * Thus, the heuristic is assigning heavier weights to relatively more
	 * preferable board layouts.
	 * 
	 * @param gameBoard
	 * @param NConnections
	 * @param playerNum
	 * 
	 */
	public int evaluateBoard(Board gameBoard, int NConnections, int playerNum) {
		int[] ourScores = new int[NConnections - 1];
		int ourTotalScore = 0;

		int[] opponentScores = new int[NConnections - 1];
		int opponentTotalScore = 0;

		int opponentPlayerNum = 3 - playerNum;

		for (int i = 2; i <= NConnections; i++) {
			ourScores[i - 2] = evaluateTokensInARow(i, playerNum, gameBoard)
					* i;
			
			//normalize scores
			if(ourScores[i - 2] > 0) ourScores[i - 2] = 1;
			else if(ourScores[i - 2] < 0) ourScores[i - 2] = -1;
			else ourScores[i - 2] = 0;
			
			ourTotalScore += ourScores[i - 2];
		}

		for (int i = 2; i <= NConnections; i++) {
			opponentScores[i - 2] = evaluateTokensInARow(i, opponentPlayerNum,
					gameBoard) * i;
			
			//normalize scores
			if(opponentScores[i - 2] > 0) opponentScores[i - 2] = 1;
			else if(opponentScores[i - 2] < 0) opponentScores[i - 2] = -1;
			else opponentScores[i - 2] = 0;
			
			opponentTotalScore += opponentScores[i - 2];
		}

		// if opponent has >= 1 N connections on the board, we lose
		if (opponentScores[NConnections - 2] >= 1) {
			return Integer.MIN_VALUE;
		} else {
			return ourTotalScore - opponentTotalScore;
		}
	}
	
	/**
	 * Simplified version of evaluateBoard(); checks only for N connections (as
	 * opposed to 2, 3, ...., N connections in evaluateBoard())
	 */
	public int evaluateBoardForOnlyWins(Board gameBoard, int NConnections,
			int playerNum) {
		int ourScore = 0;
		int opponentScore = 0;

		int opponentPlayerNum = 3 - playerNum;

		//not normalizing here since there's just one value for each score
		ourScore = evaluateTokensInARow(NConnections, playerNum, gameBoard);
		opponentScore = evaluateTokensInARow(NConnections, opponentPlayerNum,
				gameBoard);

		if (opponentScore >= 1)
			return Integer.MIN_VALUE;
		else
			return ourScore - opponentScore;
	}

	/**
	 * Another heuristic: determines preferability of a board using the number
	 * of tokens closer to the center of the board; the number is reduced based
	 * on the number of opponent tokens closer to the center of the board
	 * 
	 * The idea of the algorithm was obtained from Jenny Lam's paper on 
	 * "Heuristics in the game of Connect-K" 
	 * (http://www.ics.uci.edu/~jlam2/connectk.pdf). Code written by us.
	 * 
	 */
	public int evaluateBoardUsingCenterOfMass(Board gameBoard,
			int NConnections, int playerNum) {
		int width = gameBoard.width;
		int height = gameBoard.height;
		int center = (width / 2);

		int opponentNum = 3 - playerNum;
		int opponentScore = 0;
		int ourScore = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (gameBoard.board[i][j] == playerNum) {
					ourScore += center - Math.abs(j - center);
				} else if (gameBoard.board[i][j] == opponentNum) {
					opponentScore += center - Math.abs(j - center);
				} else{//empty
					ourScore += 0;
				}
			}
		}
		/*
		 * for connect4: 
		 * columns -> weight 
		 * 1 -> 1 
		 * 2 -> 2 
		 * 3 -> 3 
		 * 4 -> 4 
		 * 5 -> 3 
		 * 6 -> 2 
		 * 7 -> 1
		 */
		return ourScore - opponentScore;
	}
	
	/**
	 * returns the best move (a Move object) that the player (identified by
	 * playerNum) should make
	 */
	public Move bestMoveToMake(Board gameBoard, int NConnections, int playerNum) {
		// make a copy of board so we can change as needed
		Board tempBoard = gameBoard.getDuplicate();

		int boardValue = 0;

		// get list of possible moves (strings)
		List<String> possibleMoves = getPossibleMoves(tempBoard);

		// store all legal moves with corresponding values as determined after
		// making the moves
		List<Move> movesWithValues = new ArrayList<Move>();

		// store each legal move in turn
		Move theMove = new Move();

		for (String aMove : possibleMoves) {
			// reinitialize each time so that each potential move is made on a
			// copy of the unaltered game board
			tempBoard = gameBoard.getDuplicate();
			try {
				// for a proposed move, make the move on a copy of the board
				// if move is not valid, a Connect4Exception will be thrown
				// if move is valid, the move will be made on the copy of the
				// game board
				currentPlayer.makeMove(aMove, playerNum, tempBoard);
				
				// and evaluate the board value using heuristic function
				// since the move is already made, we don't have to worry about
				// whether it was a drop in or a pop out
				boardValue = evaluateBoardUsingCenterOfMass(tempBoard, NConnections, playerNum);

				//reinitialize object
				theMove = new Move();
				
				// store the move string in the move's moveString variable
				theMove.setMoveString(aMove);

				// store the board value in the move's moveValue variable
				theMove.setMoveValue(boardValue);

				// add the move to list of moves
				movesWithValues.add(theMove);

			} catch (Connect4Exception e) {
				// the potential move was illegal; ignore it
			}
		}
		
		int highestMoveValueSoFar = Integer.MIN_VALUE;
		Move proposedMove = new Move();
		
		//Note: the referee will terminate the game if a player wins
		//so the heuristic should never even have to determine a move
		//on a board that already has a connection of N tokens
		
		// among all moves, find the move with the highest value
		for (Move aMove : movesWithValues) {
			if (aMove.getMoveValue() >= highestMoveValueSoFar) { // if two moves
																// have the same
																// value, we
																// take the
																// last one we
																// encounter
				highestMoveValueSoFar = aMove.getMoveValue();
				proposedMove.setMoveString(aMove.getMoveString());
				proposedMove.setMoveValue(highestMoveValueSoFar);
				// this if statement should be executed at least once; thus
				// proposedMove will be a legal move
			}
		}
		
		return proposedMove;
	}
}
