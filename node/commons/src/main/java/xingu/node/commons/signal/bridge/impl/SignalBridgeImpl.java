package xingu.node.commons.signal.bridge.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.session.Session;
import xingu.node.commons.session.SessionManager;
import xingu.node.commons.signal.ExceptionSignal;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalWaiter;
import xingu.node.commons.signal.TimeoutSignal;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.bridge.SignalBridge;
import xingu.node.commons.signal.processor.SignalProcessor;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.TimeUtils;

public class SignalBridgeImpl
	implements SignalBridge
{
	@Inject
	private SignalProcessor			processor;

	@Inject
	private SessionManager			sessionManager;

	private static final long		queryTimeout	= TimeUtils.toMillis("1m");

	private static final Logger		logger			= LoggerFactory.getLogger(SignalBridgeImpl.class);

	private volatile AtomicLong		count			= new AtomicLong(0);

	private List<Waiter<Signal>>	waiters			= Collections.synchronizedList(new ArrayList<Waiter<Signal>>());

	@Override
	public void on(Channel channel, Signal signal)
		throws Exception
	{
		Session session = sessionManager.by(channel);
		long    id      = session.getId();
		signal.setSessionId(id);

		Waiter<Signal> waiter = findWaiter(signal);
		if(waiter != null)
		{
			synchronized(waiter)
			{
				waiter.notify(signal);
			}
		}
		else
		{
			processor.process(signal, channel);
		}
	}

	private Waiter<Signal> findWaiter(Signal signal)
	{
		for(Waiter<Signal> waiter : waiters)
		{
			if(waiter.waitingOn(signal))
			{
				return waiter;
			}
		}
		return null;
	}

	private Signal waitForReply(Signal signal, Waiter<Signal> waiter)
	{
	    waiter.waitFor(queryTimeout);
	    return waiter.reply;
	}

	@Override
	public Signal query(Channel channel, ChannelFutureListener onWrite, Signal signal)
		throws Exception
	{
		Signal reply = null;
		touch(channel, signal);
		Waiter<Signal> waiter = new SignalWaiter(signal);
		waiters.add(waiter);
		
		try
		{
			ChannelFuture future = deliver(channel, signal);
			future.addListener(onWrite);
			future.awaitUninterruptibly();
			reply = backFromTheFuture(future, signal, waiter); /* pretty cool don't you think? */
		}
		catch(Exception e)
		{
			return new ExceptionSignal(signal, e);
		}
		finally
		{
			waiters.remove(waiter);
		}

		return reply;
	}

	private Signal backFromTheFuture(ChannelFuture future, Signal signal, Waiter<Signal> waiter)
	{
		boolean success = future.isSuccess();
		if(success)
		{
			Signal reply = waitForReply(signal, waiter);
			if(reply != null)
			{
				return reply;
			}
			return new TimeoutSignal(signal);
		}
		else
		{
			Throwable cause = future.getCause();
			return new ExceptionSignal(signal, cause);
		}
	}

	@Override
	public ChannelFuture deliver(Channel channel, Signal signal)
		throws Exception
	{
		touch(channel, signal);
		return channel.write(signal);
	}

	private long touch(Channel channel, Signal signal)
	{
		long sid = signal.getSessionId();
		if(sid <= 0)
		{
			Session session = sessionManager.by(channel);
			sid = session.getId();
			signal.setSessionId(sid);
		}

		long id = signal.getSignalId();
		if(id <= 0)
		{
			id = count.incrementAndGet();
			signal.setSignalId(id);
		}
		return id;
	}
}