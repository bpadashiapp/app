package ir.tahasystem.music.app.ThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

import android.util.Log;



public class MyMonitorThread implements Runnable {
	private ThreadPoolExecutor executor;

	private int seconds=10;

	private boolean run = true;

	public static boolean isDebug = false;

	public MyMonitorThread(ThreadPoolExecutor executor, int delay) {
		this.executor = executor;
		this.seconds = delay;
	}

	public void shutdown() {
		this.run = false;
	}

	@Override
	public void run() {
		while (run) {
			Log.v("THREADPOOL",
					String.format(
							"[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
							this.executor.getPoolSize(),
							this.executor.getCorePoolSize(),
							this.executor.getActiveCount(),
							this.executor.getCompletedTaskCount(),
							this.executor.getTaskCount(),
							this.executor.isShutdown(),
							this.executor.isTerminated()));



			try {
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}



	public static void setIsDebug(boolean checked) {
		isDebug=checked;
		
	}

}