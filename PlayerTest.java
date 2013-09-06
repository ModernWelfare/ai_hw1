import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	Board b;
	Player us;

	@Before
	public void setup() {
		b = new Board(11, 11, 4);
		us = new Player(1, 5);
	}

	// /**
	// * test is connect N diagonally y=-x+k
	// *
	// * */
	// @Test
	// public void testMoves() {
	// List<String> possibleMoves = us.getPossibleMoves(b);
	// System.out.println(possibleMoves.get(0));
	// }

	/**
	 * test is connect N diagonally y=-x+k
	 * 
	 * @throws Connect4Exception
	 * */
	@Test
	public void testResult() throws Connect4Exception {
		String a = us.getMove(b);
		assertNotNull(a);
		System.out.println(a);
	}
	// /**
	// * test is connect N diagonally y=-x+k
	// * */
	// @Test
	// public void testMinimax() {
	// us.minimax(b);
	// System.out.println(us.minimax(b).moveValue);
	// System.out.println(us.minimax(b).moveString);
	// assertTrue(us.minimax(b).moveValue == 1);
	// }
}
