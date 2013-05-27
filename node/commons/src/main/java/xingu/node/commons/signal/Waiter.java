package xingu.node.commons.signal;

public abstract class Waiter<T>
{
	public T		reply;

	public T		request;

	private long	startTime;

	private long	stopTime;

	private boolean	notified	= false;

	public Waiter(T request)
	{
		this.request   = request;
		this.startTime = System.currentTimeMillis();
	}

	public void notify(T reply)
	{
		this.reply    = reply;
		this.notified = true;
		this.stopTime = System.currentTimeMillis();
		notify();
	}

	public long time()
	{
		return stopTime - startTime;
	}

	public void waitFor(long timeout)
	{
		if(notified)
		{
			return;
		}
		synchronized(this)
		{
			try
			{
				wait(timeout);
			}
			catch(InterruptedException e)
			{
				return;
			}
		}
		if(stopTime == 0)
		{
			stopTime = System.currentTimeMillis();
		}
	}

	public abstract boolean waitingOn(T reply);
}