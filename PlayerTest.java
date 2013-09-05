import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	Board b;
	Player us;

	@Before
	public void setup() {
		b = new Board(4, 4, 3);
		us = new Player(1, 10);
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * */
	@Test
	public void testMoves() {
		List<String> possibleMoves = us.getPossibleMoves(b);
		System.out.println(possibleMoves.get(0));
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * 
	 * @throws Connect4Exception
	 * */
	@Test
	public void testResult() throws Connect4Exception {
		// Board newBoard = us.result("0 1", b, 1);
		// newBoard.printBoard();
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * */
	@Test
	public void testMinimax() {
		us.minimax(b);
		System.out.println(us.minimax(b).moveValue);
		System.out.println(us.minimax(b).moveString);
		assertTrue(us.minimax(b).moveValue == 1);
	}

	// /**
	// * test is connect N diagonally y=-x+k
	// * */
	// @Test
	// public void test1() {
	// b.dropADiscFromTop(2, 1);
	// b.dropADiscFromTop(1, 2);
	// b.dropADiscFromTop(1, 1);
	// b.dropADiscFromTop(0, 2);
	// b.dropADiscFromTop(0, 1);
	// b.dropADiscFromTop(2, 2);
	// b.dropADiscFromTop(0, 1);
	// b.printBoard();
	// int tmp_winner = b.checkDiagonally1();
	// System.out.println("Winner: " + tmp_winner);
	// }
	//
	// /**
	// * test is connect N diagonally y=-x+k
	// * */
	// @Test
	// public void test2() {
	// b.setBoard(1, 2, b.PLAYER1);
	// b.setBoard(2, 3, b.PLAYER1);
	// b.setBoard(3, 4, b.PLAYER1);
	// b.printBoard();
	// int tmp_winner = b.checkDiagonally1();
	// // int tmp_winner= isConnectN();
	// System.out.println("Winner: " + tmp_winner);
	// }
	//
	// /**
	// * test is connect N diagonally y=x-k
	// * */
	// @Test
	// public void test3() {
	// // setBoard(2,5,this.PLAYER2);
	// // setBoard(4,3,this.PLAYER2);
	// // setBoard(3,4,this.PLAYER2);
	// b.setBoard(5, 4, b.PLAYER1);
	// b.setBoard(4, 5, b.PLAYER1);
	// b.setBoard(3, 6, b.PLAYER1);
	// b.printBoard();
	// int tmp_winner = b.checkDiagonally2();
	// // int tmp_winner= isConnectN();
	// System.out.println("Winner: " + tmp_winner);
	// }
	//
	// /**
	// * test is connect N diagonally y=-x+k
	// * */
	// @Test
	// public void test4() {
	// b.setBoard(2, 0, b.PLAYER1);
	// b.setBoard(3, 1, b.PLAYER1);
	// // setBoard(4,2,this.PLAYER1);
	// b.setBoard(5, 3, b.PLAYER1);
	// b.printBoard();
	// int tmp_winner = b.checkDiagonally1();
	// // int tmp_winner= isConnectN();
	// System.out.println("Winner: " + tmp_winner);
	// }

}
