package br.com.ibnetwork.xingu.lang.thread;

public interface Task
{
	void execute()
		throws Exception;

	void pause(long time)
		throws InterruptedException;

	boolean isFinished();
}
