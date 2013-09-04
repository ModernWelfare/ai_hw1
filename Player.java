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
	int timeLimit;
	boolean isMax;

	/**
	 * Constructor
	 */
	public Player(int playerNumber, int timeLimit, boolean isMax) {
		this.playerNumber = playerNumber;
		this.timeLimit = timeLimit;
		this.isMax = isMax;
	}

	/**
	 * Function to get a valid move
	 * 
	 * @param timeout
	 * @param gameBoard
	 * @return a string representing the move
	 */
	public String getMove(Board gameBoard) {
		String move = "2 1";
		return move;
	}
	
	/**
	 * Function to check if a particular drop move is legally allowed on the board
	 * Since empty spots are represented by '9', if there is an empty spot in the column
	 * that the drop is being attempted in, it will be allowed.
	 * 
	 * @param gameBoard
	 * @param dropInColumn the column number that the drop move is being attempted in
	 * 
	 * @return boolean indicating if the drop move is legal
	 * 
	 */
	public boolean isDropMoveLegal(Board gameBoard, int dropInColumn){
		for (int i : gameBoard.height){
			if (gameBoard[i][dropInColumn] == 9){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found vertically, 0 otherwise
	 */
	public int evaluateTokensInARowVertically(int numTokens, Board gameBoard, int numRow, int numColumn){
		int countOfTokensEncounteredVertically = 0;
		
		for(int i = numRow; i < gameBoard.height; i++){
			if(gameBoard[numRow][numColumn] == gameBoard[i][numColumn]){
				countOfTokensEncounteredVertically++;
			}
			else{
				//since the continuity is broken, stop
				break;
				
				//TODO: is this check complete? Should we also be looking down the column
				//to determine vertical n-in-a-row tokens?
			}
		}
		
		if(countOfTokensEncounteredVertically >= numTokens){
			return 1;
		}
		else{
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
	public int evaluateTokensInARowHorizontally(int numTokens, Board gameBoard, int numRow, int numColumn){
		int countOfTokensEncounteredHorizontally = 0;
		
		for(int i = numColumn; i < gameBoard.width; i++){
			if(gameBoard[numRow][numColumn] == gameBoard[numRow][i]){
				countOfTokensEncounteredHorizontally++;
			}
			else{
				//since the continuity is broken, stop
				break;
				
				//TODO: is this check complete? Should we also be looking at the LHS of
				//the column to determine horizontal n-in-a-row tokens?
			}
		}
		
		if(countOfTokensEncounteredHorizontally >= numTokens){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found in the left diagonal, 0 otherwise
	 */
	public int evaluateTokensInARowDiagonallyLeft(int numTokens, Board gameBoard, int numRow, int numColumn){
		int countOfTokensEncounteredDiagonallyLeft = 0;
		
		int j = numColumn;
		
		for(int i = numRow; i > -1; i--, j++){
			if(j > gameBoard.height){ //check to make sure not out of bounds
				break;
			}
			else if(gameBoard[numRow][numColumn] == gameBoard[i][j]){
				countOfTokensEncounteredDiagonallyLeft++;
			}
			else{
				break;
			}
		}
		
		if(countOfTokensEncounteredDiagonallyLeft >= numTokens){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * @param numTokens
	 * @param gameBoard
	 * @param numRow
	 * @param numColumn
	 * 
	 * @return 1 if numTokens number of tokens found in the right diagonal, 0 otherwise
	 */
	public int evaluateTokensInARowDiagonallyRight(int numTokens, Board gameBoard, int numRow, int numColumn){
		int countOfTokensEncounteredDiagonallyRight = 0;
		
		int j = numColumn;
		
		for(int i = numRow; i < gameBoard.height; i++, j++){
			if(j > gameBoard.height){ //check to make sure not out of bounds
				break;
			}
			else if(gameBoard[numRow][numColumn] == gameBoard[i][j]){
				countOfTokensEncounteredDiagonallyRight++;
			}
			else{
				break;
			}
		}
		
		if(countOfTokensEncounteredDiagonallyRight >= numTokens){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * @param numTokens the N-in-a-row number of tokens being searched for
	 * @param playerNum the player whose tokens are being searched
	 * @param gameBoard
	 * 
	 * @return the number of tokens encountered for the specified N-in-a-row
	 */
	public int evaluateTokensInARow(int numTokens, int playerNum, Board gameBoard){
		int countOfTokensEncountered = 0;
		
		for(int i = 0; i < gameBoard.height; i++){
			for(int j = 0; j < gameBoard.width; i++){
				if(gameBoard[i][j] == playerNum){//check only our tokens; ignore the opponent's tokens
					countOfTokensEncountered += evaluateTokensInARowVertically(numTokens, gameBoard, i, j);
					countOfTokensEncountered += evaluateTokensInARowHorizontally(numTokens, gameBoard, i, j);
					countOfTokensEncountered += evaluateTokensInARowDiagonallyLeft(numTokens, gameBoard, i, j);
					countOfTokensEncountered += evaluateTokensInARowDiagnoallyRight(numTokens, gameBoard, i, j);
				}
			}
		}
		
		return countOfTokensEncountered;
	}
	
	/**
	 * Evaluates the relative preferability of the board
	 * fours-in-a-row are weighted more than threes-in-a-row, which in turn
	 * are weighted more than twos-in-a-row
	 * 
	 *  Currently using a relatively simple heuristic: assign arbitrary weights to the
	 *  relatively preferable board configurations
	 *  
	 * @param gameBoard
	 * @param playerNum
	 * 
	 */
	public int evaluateBoard(Board gameBoard, int playerNum){
		int ourScoreFoursInARow = evaluateTokensInARow(4, playerNum, gameBoard) * 10000;
		int ourScoreThreesInARow = evaluateTokensInARow(3, playerNum, gameBoard) * 1000;
		int ourScoreTwosInARow = evaluateTokensInARow(2, playerNum, gameBoard) * 100;
		int ourTotalScore = ourScoreFoursInARow + ourScoreThreesInARow + ourScoreTwosInARow;
		
		//TODO: is this the acceptable way of determining the opponent's number?
		int opponentPlayerNum = (playerNum == 1?) 2 : 1;
		
		int opponentScoreFoursInARow = evaluateTokensInARow(4, opponentPlayerNum, gameBoard) * 10000 * 10; //bumping up by factor of 10 since this would mean our player would lose in that configuration
		int opponentScoreThreesInARow = evaluateTokensInARow(3, opponentPlayerNum, gameBoard) * 1000;
		int opponentScoreTwosInARow = evaluateTokensInARow(2, opponentPlayerNum, gameBoard) * 100;
		int opponentTotalScore = opponentScoreFoursInARow + opponentScoreThreesInARow + opponentScoreTwosInARow;
		
		return ourTotalScore - opponentTotalScore;
	}

	/**
	 * Function for the player to make a move based on a string
	 * 
	 * @param moveToTake
	 *            The move the player is going to make
	 * @param playerNum
	 * 			   represents the current player
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
		} else { //the move made was to remove a disc
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
	 * Minimax function for deciding the next move
	 * 
	 * @param gameBoard
	 * @param isMax
	 * @return
	 */
	public int minimax(Board gameBoard, boolean isMax, int playerNum) {
		int valueToReturn;
		int terminalTestValue = gameBoard.isConnectN();
		List<String> possibleMoves = getPossibleMoves(gameBoard);

		// if the game is over with a connection
		if (terminalTestValue != -1) {
			valueToReturn = (terminalTestValue == playerNumber) ? 1 : 0;
		} else {
			List<Integer> minimaxResults = new ArrayList<Integer>();
			for (String move : possibleMoves) {
				try {
					int minimaxValue = minimax(
							result(move, gameBoard, 3 - playerNum), !isMax,
							3 - playerNum);
					minimaxResults.add(minimaxValue);
				} catch (Connect4Exception e) {
					e.getMessage();
				}
			}
			if (isMax) {
				// choose the max value
				int maxValue = 0;
				for (int value : minimaxResults) {
					if (value > maxValue) {
						maxValue = value;
					}
				}
				valueToReturn = maxValue;
			} else {
				// choose the min value
				int minValue = 1;
				for (int value : minimaxResults) {
					if (value < minValue) {
						minValue = value;
					}
				}
				valueToReturn = minValue;
			}
		}
		return valueToReturn;
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
		newBoard.printBoard();
		return newBoard;
	}
}
