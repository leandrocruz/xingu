package xavante.comet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CometClient
{
	public final String		id;

	private Queue<String>	queue	= new ConcurrentLinkedQueue<String>();

	public CometClient(String id)
	{
		this.id = id;
	}

	public String id()
	{
		return id;
	}

	public synchronized String[] drain()
	{
		if(queue.isEmpty())
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				return null;
			}
		}
		
		String[] messages = queue.toArray(new String[]{});
		queue.clear();
		return messages;
	}

	public synchronized void offer(String message)
	{
		queue.offer(message);
		notifyAll();
	}
}