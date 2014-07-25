package xingu.node.commons.signal.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.jboss.netty.channel.Channel;

import xingu.node.commons.session.Session;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalHandler;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.processor.SignalProcessor;
import br.com.ibnetwork.xingu.container.Inject;

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

		Waiter<Signal> waiter = waiters.findWaiter(signal);
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
}