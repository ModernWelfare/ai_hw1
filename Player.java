import java.util.ArrayList;
import java.util.List;

/**
 * Class to implement the player of the game
 * 
 * @author bli
 * @author tnarayan
 * 
 */
public class Player {
	int playerNumber;
	int opponentNumber;
	int timeLimit;
	int DEPTH;

	// List<Board> minGameBoards;
	// List<Board> maxGameBoards;

	/**
	 * Constructor for the player
	 * 
	 * @param playerNumber
	 *            The number of the player, either 1 or 2
	 * @param timeLimit
	 *            The time limit given to the player to make the move
	 */
	public Player(int playerNumber, int timeLimit) {
		this.playerNumber = playerNumber;
		this.opponentNumber = 3 - playerNumber;
		this.timeLimit = timeLimit;
		this.DEPTH = 6;
	}

	/**
	 * Function to get a valid move. It uses a limited amount of time to execute
	 * the minimax function
	 * 
	 * @param gameBoard
	 *            the board on which the game is carried out
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		Move moveToTake = new Move("0 0", 999);
		MakeMoveThread mmt = new MakeMoveThread(gameBoard, new Player(
				playerNumber, timeLimit));
		// TODO: hook to bestMoveToMake() inside MakeMoveThread, in expires()
		// inside if(result == null)
		TimedTaskExecuter.execute(mmt, (timeLimit - 1) * 1000);
		try {
			Thread.sleep((timeLimit - 1) * 1000 + 500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveToTake = mmt.result;
		return moveToTake.moveString;
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
			for (int j = 0; j < gameBoard.width; i++) {
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
		// TODO: this may overflow if NConnections is big enough
		// Can take care of this by normalizing values

		int[] ourScores = new int[NConnections - 1];
		int ourTotalScore = 0;

		int[] opponentScores = new int[NConnections - 1];
		int opponentTotalScore = 0;

		int opponentPlayerNum = 3 - playerNum;

		for (int i = 2; i <= NConnections; i++) {
			// int ourScoreThreesInARow = evaluateTokensInARow(3, playerNum,
			// gameBoard) * 1000;
			ourScores[i - 2] = evaluateTokensInARow(i, playerNum, gameBoard)
					* i;
			ourTotalScore += ourScores[i - 2];
		}

		for (int i = 2; i <= NConnections; i++) {
			opponentScores[i - 2] = evaluateTokensInARow(i, opponentPlayerNum,
					gameBoard) * i;
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
	 * of tokens closer to the centre of the board; the number is reduced based
	 * on the number of opponent tokens closer to the centre of the board
	 */
	public int evaluateBoardUsingCentreOfMass(Board gameBoard,
			int NConnections, int playerNum) {
		int width = gameBoard.width; // for connect4, this is 7
		int height = gameBoard.height; // for connect4, this is 6
		int centre = (width / 2) + 1; // for connect4, this is 4

		int opponentNum = 3 - playerNum;
		int opponentScore = 0;
		int ourScore = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (gameBoard.board[i][j] == playerNum) {
					ourScore += centre - Math.abs(j - centre);
				} else if (gameBoard.board[i][j] == opponentNum) {
					opponentScore += centre - Math.abs(j - centre);
				}
			}
		}
		/*
		 * for connect4: columns -> weight 1 -> 1 2 -> 2 3 -> 3 4 -> 4 5 -> 3 6
		 * -> 2 7 -> 1
		 */
		return ourScore - opponentScore;
	}

	/**
	 * A fourth heuristic Currently evaluates a proposedMove as opposed to a
	 * board's state after making the move
	 */
	/*
	 * public int evaluateBoardAndMoveUsingEightPattern(Board gameBoard, int
	 * NConnections, int playerNum, Move proposedMove){ String
	 * proposedMoveString = proposedMove.getMoveString(); String[] moveData =
	 * proposedMoveString.split(" "); int moveNum =
	 * Integer.parseInt(moveData[0]);
	 * 
	 * int height = gameBoard.height; int proposedMoveColumn = moveNum; int
	 * proposedMoveRow;
	 * 
	 * int opponentNum = 3 - playerNum;
	 * 
	 * int preferabilityCount = 0;
	 * 
	 * for(int i = 0; i < height; i++){
	 * if(gameBoard.board[i][proposedMoveColumn] == gameBoard.emptyCell){
	 * //first empty spot in the column proposedMoveRow = i; break; } }
	 * 
	 * //now check the 8 spots around this (x, y) coordinate pair //if our
	 * tokens are present, the move is more favorable //if opponent's tokens are
	 * present, the move is less favorable //if all spaces are empty, the move
	 * is okay //the 8 spots around the coordinate pair may not all exist
	 * for(int i = proposedMoveRow - 1, j = proposedMoveColumn - 1; (i <
	 * proposedMoveRow + 1) && (j < proposedMoveColumn + 1); i++, j++){ if(i ==
	 * proposedMoveRow && j == proposedMoveColumn){ //do not compare against
	 * yourself! continue; } else{ if(gameBoard.board[i][j] == playerNum)
	 * preferabilityCount += 2; else if(gameBoard.board[i][j] ==
	 * gameBoard.emptyCell) preferabilityCount += 1; else preferabilityCount +=
	 * 0; //opponent's token; doesn't affect preferability } } return
	 * preferabilityCount; }
	 */
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
				makeMove(aMove, playerNum, tempBoard);

				// and evaluate the board value using heuristic function
				// since the move is already made, we don't have to worry about
				// whether it was a drop in or a pop out
				boardValue = evaluateBoard(tempBoard, NConnections, playerNum);

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

		int highestMoveValueSoFar = 0;
		Move proposedMove = new Move();

		// among all moves, find the move with the highest value
		for (Move aMove : movesWithValues) {
			if (aMove.getMoveValue() > highestMoveValueSoFar) { // if two moves
																// have the same
																// value, we
																// take the
																// first one we
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

	/**
	 * Function for the player to make a move based on a string.
	 * 
	 * @param moveToTake
	 *            The move the player is going to make
	 * @param playerNum
	 *            represents the current player
	 * @param gameBoard
	 *            The board on which the player makes the move on
	 * @throws Connect4Exception
	 *             Throw exception if the move isn't legal
	 */
	public void makeMove(String moveToTake, int playerNum, Board gameBoard)
			throws Connect4Exception {
		String[] moveArrs = moveToTake.split(" ");
		int col = Integer.parseInt(moveArrs[0]);
		boolean isDrop = (Integer.parseInt(moveArrs[1]) == 1) ? true : false;

		if (isDrop) {
			if (gameBoard.canDropADiscFromTop(col, playerNum)) {
				gameBoard.dropADiscFromTop(col, playerNum);
			} else {
				throw new Connect4Exception("Drop: Move illegal");
			}
		} else { // the move made was to remove a disc
			if (gameBoard.canRemoveADiscFromBottom(col, playerNum)) {
				gameBoard.removeADiscFromBottom(col);
			} else {
				throw new Connect4Exception("Remove: Move illegal");
			}
		}
	}

	/**
	 * Function to print the information of the player
	 */
	public void printInfo() {
		System.out.println(playerNumber + ": " + timeLimit + "\n");
	}

	/**
	 * Function for min to propose a move given the board
	 * 
	 * @param gameBoard
	 *            the board on which the game is played
	 * @param stepsLeft
	 *            the depth of the search, when stepsLeft reaches 0, the
	 *            function returns a possibly invalid move with a middle value
	 * @return the proposed move of min
	 */
	public Move minMove(Board gameBoard, int stepsLeft) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Move bestMove = new Move("0 0", 2);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else if (stepsLeft == 0) {
			bestMove = new Move("0 0", 1);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMax = maxMove(
							result(move, gameBoard, opponentNumber),
							stepsLeft - 1);
					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMax.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		return bestMove;
	}

	/**
	 * Function for max to propose a move given the board
	 * 
	 * @param gameBoard
	 *            the board on which the game is played
	 * @param stepsLeft
	 *            the depth of the search, when stepsLeft reaches 0, the
	 *            function returns a possibly invalid move with a middle value
	 * @return the proposed move of max
	 */
	public Move maxMove(Board gameBoard, int stepsLeft) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", 0);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			// System.out.println(terminalTestValue);
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else if (stepsLeft == 0) {
			bestMove = new Move("0 0", 1);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMin = minMove(result(move, gameBoard, playerNumber),
							stepsLeft - 1);
					// System.out.println(moveOfMin.moveValue);
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMin.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
		}
		return bestMove;
	}

	/**
	 * Minimax function for deciding the next move
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move minimax(Board gameBoard) {
		return maxMove(gameBoard, DEPTH);
	}

	/**
	 * Minimax using alpha-beta pruning
	 * 
	 * @param gameBoard
	 * @param stepsLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Move abMinMove(Board gameBoard, int stepsLeft, int alpha, int beta) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMax;
		Move bestMove = new Move("0 0", Integer.MAX_VALUE);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else if (stepsLeft == 0) {
			bestMove = new Move("0 0", 1);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMax = maxMove(
							result(move, gameBoard, opponentNumber),
							stepsLeft - 1);
					if (moveOfMax.moveValue < bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMax.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				if (bestMove.moveValue <= alpha) {
					return bestMove;
				}
				beta = Math.min(beta, bestMove.moveValue);
			}
		}
		return bestMove;
	}

	/**
	 * Minimax using alpha-beta pruning
	 * 
	 * @param gameBoard
	 * @param stepsLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Move abMaxMove(Board gameBoard, int stepsLeft, int alpha, int beta) {
		int terminalTestValue = gameBoard.isConnectN();
		int bestValueOfMove;
		Move moveOfMin;
		Move bestMove = new Move("0 0", Integer.MIN_VALUE);
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			// System.out.println(terminalTestValue);
			bestValueOfMove = (terminalTestValue == playerNumber) ? 2 : 0;
			bestMove = new Move("0 0", bestValueOfMove);
		} else if (stepsLeft == 0) {
			bestMove = new Move("0 0", 1);
		} else {
			for (String move : possibleMoves) {
				try {
					moveOfMin = minMove(result(move, gameBoard, playerNumber),
							stepsLeft - 1);
					// System.out.println(moveOfMin.moveValue);
					if (moveOfMin.moveValue > bestMove.moveValue) {
						bestMove.moveString = move;
						bestMove.moveValue = moveOfMin.moveValue;
					}
				} catch (Connect4Exception e) {
					e.getMessage();
				}
				if (bestMove.moveValue >= beta) {
					return bestMove;
				}
				alpha = Math.max(bestMove.moveValue, alpha);
			}
		}
		// gameBoard.printBoard();
		// System.out.println("Bestmove" + bestMove.moveValue);
		// System.out.println("Bestmove" + bestMove.moveString);
		return bestMove;
	}

	/**
	 * Minimax function using alpha beta pruning
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move abMinimax(Board gameBoard) {
		return abMaxMove(gameBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * returns a list of possible moves on the board note that not all of these
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
		makeMove(move, playerNum, newBoard);
		// for (Board checkedBoard : checkedBoards) {
		// if (checkedBoard.equals(newBoard)) {
		// throw new Connect4Exception(
		// "Board already checked for the player");
		// }
		// }
		// checkedBoards.add(newBoard);
		return newBoard;
	}
}
