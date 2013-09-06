/**
 * Third party code, cited from http://bbs.csdn.net/topics/300023474
 * 
 * @author Dan 1980
 * 
 */
public class TimedTaskExecuter {

	private static class ExpiringThread extends Thread {
		TimedRunnable timed;
		long expired;

		ExpiringThread(TimedRunnable task, long expiredTime) {
			timed = task;
			expired = expiredTime;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(expired);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timed.expire();
		}
	}

	public static void execute(TimedRunnable task, long expiredTime) {
		new Thread(task).start();
		new ExpiringThread(task, expiredTime).start();
	}
}