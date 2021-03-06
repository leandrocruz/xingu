package xingu.lang.thread;

public class Worker
	extends Thread
{
	private volatile boolean shouldRun = true;

	private long step = 400; // 400 ms interval

	private final Task task;

	public Worker(String name, boolean daemon, long step, Task task)
	{
		setDaemon(daemon);
		setName(name);
		this.task = task;
		if (step > 0)
		{
			this.step = step;
		}
	}

	@Override
	public void run()
	{
		while (shouldRun && !task.isFinished())
		{
			try
			{
				task.execute();
				if(task.isFinished())
				{
					break;
				}
				task.pause(step);
			}
			catch (Throwable t)
			{
				/*
				 * Any error, including InterruptedException, will abort execution
				 */
				shouldRun = !task.abortOnError(t); 
			}
		}
		task.beforeExit();
	}
	
	public void finish()
	{
		this.shouldRun = false;
	}
	
	public boolean waitFinished(long millis)
	{
		return task.isFinished();
	}
}
