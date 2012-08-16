package br.com.ibnetwork.xingu.lang.thread;

public abstract class TaskSupport
	implements Task
{
	protected volatile boolean finished = false;
	
	@Override
	public boolean isFinished()
	{
		return finished;
	}

	@Override
	public void pause(long time)
		throws InterruptedException
	{
		Thread.sleep(time);
	}
}
