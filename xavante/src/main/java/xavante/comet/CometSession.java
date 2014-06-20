package xavante.comet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;

import xingu.node.commons.identity.Identity;

public class CometSession
{
	public final String		id;

	private Identity<?>		identity;

	private Queue<String>	queue	= new ConcurrentLinkedQueue<String>();

	public CometSession(String id)
	{
		this.id = id;
	}

	public String getId()
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

	public Identity<?> getIdentity()
	{
		return identity;
	}

	public void setIdentity(Identity<?> identity)
	{
		this.identity = identity;
	}

	@Override
	public String toString()
	{
		return StringUtils.abbreviate(id, 8) + " owned by " + identity;
	}
}