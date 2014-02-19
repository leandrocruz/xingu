package xingu.node.commons.signal.bridge.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.channel.Channel;

import xingu.node.commons.session.Session;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.bridge.SignalBridge;
import xingu.node.commons.signal.processor.SignalProcessor;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.TimeUtils;

public class SignalBridgeImpl
	extends BridgeSupport
	implements SignalBridge, Configurable
{
	@Inject
	private SignalProcessor			processor;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String tm = conf.getChild("query").getAttribute("timeout", "1m");
		queryTimeout = TimeUtils.toMillis(tm);
	}

	@Override
	public void on(Channel channel, Signal signal)
		throws Exception
	{
		Session session = sessions.by(channel);
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
}