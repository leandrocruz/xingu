package xavante.comet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;

import xingu.node.commons.identity.Identity;

public class CometSession
{
	public final String		id;

	private Identity<?>		identity;

	private Queue<Object>	queue	= new ConcurrentLinkedQueue<Object>();

	public CometSession(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public synchronized Object[] drain()
		throws Exception
	{
		if(queue.isEmpty())
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				throw e;
			}
		}
		
		Object[] messages = queue.toArray();
		queue.clear();
		return messages;
	}

	public synchronized void offer(Object message)
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