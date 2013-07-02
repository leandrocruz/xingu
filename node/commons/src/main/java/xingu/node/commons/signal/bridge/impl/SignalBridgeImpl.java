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
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalWaiter;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.bridge.SignalBridge;
import xingu.node.commons.signal.processor.SignalProcessor;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
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

	private ChannelFutureListener	onWrite			= new ChannelFutureListener()
	{
		@Override
		public void operationComplete(ChannelFuture future)
			throws Exception
		{
			boolean success = future.isSuccess();
			if(!success)
			{
				//TODO: mark signal as error
				Throwable error = future.getCause();
				logger.error("Error sending signal", error);
			}
		}
	};

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
	public Signal query(Channel channel, Signal signal)
		throws Exception
	{
		Signal reply = null;
		touch(channel, signal);
		Waiter<Signal> waiter = new SignalWaiter(signal);
		waiters.add(waiter);
		try
		{
			ChannelFuture future = deliver(channel, signal);
			future.awaitUninterruptibly();
			if(future.isDone() && future.isSuccess())
			{
				reply = waitForReply(signal, waiter);
			}
			else
			{
				// TODO: mark query as failed
				throw new NotImplementedYet();
			}
		}
		catch(Exception e)
		{
			// TODO: mark query as failed
			logger.error("Error waiting for query reply '" + signal + "'", e);
		}
		finally
		{
			waiters.remove(waiter);
		}

		// TODO: if reply is null then mark query as failed
		return reply;
	}

	@Override
	public ChannelFuture deliver(Channel channel, Signal signal)
		throws Exception
	{
		touch(channel, signal);
		ChannelFuture future = channel.write(signal);
		future.addListener(onWrite);
		return future;
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