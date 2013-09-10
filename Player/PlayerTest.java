/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */


package Player;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import MoveStrategies.NoLoopMinimaxStrategy;
import Util.Board;
import Util.Connect4Exception;

public class PlayerTest {

	Board b;
	Board c;
	Player us;
	MockPlayer mock;

	@Before
	public void setup() {
		b = new Board(3, 3, 3);
		c = new Board(3, 3, 3);
		us = new Player(1, 5);
		mock = new MockPlayer(1, 5);
		mock.setStrategy(new NoLoopMinimaxStrategy(b, mock));
	}

	/**
	 * test is connect N diagonally y=-x+k
	 * 
	 * @throws Connect4Exception
	 * */
	@Test
	public void testResult() throws Connect4Exception {
		String a = mock.getMove(b);
		assertNotNull(a);
		b.dropADiscFromTop(1, 1);
		c.dropADiscFromTop(2, 1);
		System.out.println('a');
		assertTrue(!b.equals(c));
	}
}
