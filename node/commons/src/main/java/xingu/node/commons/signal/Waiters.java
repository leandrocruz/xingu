package xingu.node.commons.signal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Waiters<T>
{
	private List<Waiter<T>> waiters	= new ArrayList<Waiter<T>>();

	public synchronized void add(Waiter<T> waiter)
	{
		waiters.add(waiter);
	}

	public synchronized void remove(Waiter<T> waiter)
	{
		waiters.remove(waiter);
	}

	public synchronized Waiter<T> popWaiter(T t)
	{
		Iterator<Waiter<T>> it = waiters.iterator();
		while(it.hasNext())
		{
			Waiter<T> waiter = it.next();
			if(waiter.waitingOn(t))
			{
				it.remove();
				return waiter;
			}
		}
		return null;
	}
	
	public T waitForReply(Waiter<T> waiter, long timeout)
	{
	    waiter.waitFor(timeout);
	    T reply = waiter.reply;
		if(reply != null)
		{
			/* Remove if no timeout. On timeout , we will need it further to mark signals as late */
			remove(waiter);
	    }
		else
		{
			waiter.isLate = true;
		}
	    return reply;
	}
}
