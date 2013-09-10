/**
 * This code is created for CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 */

package MoveStrategies;
import Util.Move;

/**
 * Move strategy interface Used to adopt different strategies in making a move
 * 
 * @author Li Bohao
 * 
 */
public interface MoveStrategy {
	public Move proposeMove();
}
