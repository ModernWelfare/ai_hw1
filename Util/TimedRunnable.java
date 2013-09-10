/**
 * For CS4341 at WPI, A term 2013.
 * Team members: Bohao Li (bli@wpi.edu), Tushar Narayan (tnarayan@wpi.edu)
 * 
 * This code is actually taken from online source as cited below.
 */

package Util;
/**
 * Third party code, cited from http://bbs.csdn.net/topics/300023474
 * 
 * @author Dan 1980
 * 
 */
public interface TimedRunnable extends Runnable {
	void expire();
}