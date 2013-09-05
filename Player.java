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

	// List<Board> minGameBoards;
	// List<Board> maxGameBoards;

	public Player(int playerNumber, int timeLimit) {
		this.playerNumber = playerNumber;
		this.opponentNumber = 3 - playerNumber;
		this.timeLimit = timeLimit;
	}

	/**
	 * Function to get a valid move
	 * 
	 * @param timeout
	 * @param gameBoard
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		Move moveToTake = abMinimax(gameBoard);
		// Move moveToTake = minimax(gameBoard);
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
			if (j > gameBoard.height) { // check to make sure not out of bounds
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
	 * Evaluates the relative preferability of the board fours-in-a-row are
	 * weighted more than threes-in-a-row, which in turn are weighted more than
	 * twos-in-a-row
	 * 
	 * Extending this to connect-N: twos-in-a-row, threes-in-a-row, ..., n-in-a-row
	 * 
	 * Currently using a relatively simple heuristic: assign arbitrary weights
	 * to the relatively preferable board configurations
	 * 
	 * @param gameBoard
	 * @param NConnections
	 * @param playerNum
	 * 
	 */
	public int evaluateBoard(Board gameBoard, int NConnections, int playerNum) {
		//TODO: this may overflow if NConnections is big enough
		
		int ourScores[NConnections - 1];
		int ourTotalScore = 0;
		
		int opponentScores[NConnections - 1];
		int opponentTotalScore = 0;
		
		int opponentPlayerNum = (playerNum == 1) ? 2 : 1;
		
		for(int i = 2; i <= NConnections; i++){
			//int ourScoreThreesInARow = evaluateTokensInARow(3, playerNum, gameBoard) * 1000;
			ourScores[i - 2] = evaluateTokensInARow(i, playerNum, gameBoard) * Math.pow(10, i);
			ourTotalScore += ourScores[i - 2];
		}
		
		for(int i = 2; i<= NConnections; i++){
			opponentScores[i - 2] = evaluateTokensInARow(i, opponentPlayerNum, gameBoard) * Math.pow(10,  i);
			opponentTotalScore += opponentScores[i -2];
		}

		//if opponent has >= 1 N connections on the board, we lose
		if(evaluateTokensInARow(NConnections, opponentPlayerNum, gameBoard) >= 1){
			return Integer.MIN_VALUE;
		}
		else{
			return ourTotalScore - opponentTotalScore;
		}
	}

	/**
	 * Function for the player to make a move based on a string
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
		// gameBoard.printBoard();
		// System.out.println("Bestmove" + bestMove.moveValue);
		// System.out.println("Bestmove" + bestMove.moveString);
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
		return maxMove(gameBoard, gameBoard.width * gameBoard.height / 2);
	}

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
	 * Minimax function for deciding the next move
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public Move abMinimax(Board gameBoard) {
		return abMaxMove(gameBoard, gameBoard.width * gameBoard.height / 2,
				Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

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
