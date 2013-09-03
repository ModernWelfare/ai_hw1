import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	Board b;

	@Before
	public void setup() {
		b = new Board(6, 7, 3);
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * */
	@Test
	public void test1() {
		b.dropADiscFromTop(2, 1);
		b.dropADiscFromTop(1, 2);
		b.dropADiscFromTop(1, 1);
		b.dropADiscFromTop(0, 2);
		b.dropADiscFromTop(0, 1);
		b.dropADiscFromTop(2, 2);
		b.dropADiscFromTop(0, 1);
		b.printBoard();
		int tmp_winner = b.checkDiagonally1();
		System.out.println("Winner: " + tmp_winner);
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * */
	@Test
	public void test2() {
		b.setBoard(1, 2, b.PLAYER1);
		b.setBoard(2, 3, b.PLAYER1);
		b.setBoard(3, 4, b.PLAYER1);
		b.printBoard();
		int tmp_winner = b.checkDiagonally1();
		// int tmp_winner= isConnectN();
		System.out.println("Winner: " + tmp_winner);
	}

	/**
	 * test is connect N diagonally y=x-k
	 * */
	@Test
	public void test3() {
		// setBoard(2,5,this.PLAYER2);
		// setBoard(4,3,this.PLAYER2);
		// setBoard(3,4,this.PLAYER2);
		b.setBoard(5, 4, b.PLAYER1);
		b.setBoard(4, 5, b.PLAYER1);
		b.setBoard(3, 6, b.PLAYER1);
		b.printBoard();
		int tmp_winner = b.checkDiagonally2();
		// int tmp_winner= isConnectN();
		System.out.println("Winner: " + tmp_winner);
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * */
	@Test
	public void test4() {
		b.setBoard(2, 0, b.PLAYER1);
		b.setBoard(3, 1, b.PLAYER1);
		// setBoard(4,2,this.PLAYER1);
		b.setBoard(5, 3, b.PLAYER1);
		b.printBoard();
		int tmp_winner = b.checkDiagonally1();
		// int tmp_winner= isConnectN();
		System.out.println("Winner: " + tmp_winner);
	}

}
