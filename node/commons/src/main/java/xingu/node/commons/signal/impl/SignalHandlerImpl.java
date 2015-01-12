package xingu.node.commons.signal.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.jboss.netty.channel.Channel;

import xingu.container.Inject;
import xingu.node.commons.session.Session;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalHandler;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.processor.SignalProcessor;

public class SignalHandlerImpl
	extends SignalHandlerSupport
	implements SignalHandler, Configurable
{
	@Inject
	private SignalProcessor			processor;

	@Override
	public void on(Signal signal, Channel channel)
		throws Exception
	{
		Session session = sessions.by(channel);
		long    id      = session.getId();
		signal.setSessionId(id);

		Waiter<Signal> waiter  = waiters.popWaiter(signal);
		boolean        process = waiter == null ? true : waiter.isLate;
		if(waiter != null)
		{
			synchronized(waiter)
			{
				waiter.notify(signal);
			}

			/*processing of late signals when there is a waiter */
			signal.setLate(waiter.isLate);
		}

		if(process)
		{
			/*
			 * If signals replies are 'late', there is no waiter anymore and they are processed here!
			 */
			processor.process(signal, channel);
		}
	}
}