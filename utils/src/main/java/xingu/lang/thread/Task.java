package xingu.lang.thread;

public interface Task
{
	void execute()
		throws Exception;

	void pause(long time)
		throws InterruptedException;

	boolean isFinished();

	boolean abortOnError(Throwable t);

	void beforeExit();
}
