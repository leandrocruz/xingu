package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.container.Inject;
import xingu.lang.NotImplementedYet;
import xingu.node.client.bridge.BridgeConnector;
import xingu.node.commons.signal.ReverseSignalHandler;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalHandler;

public class ReverseSignalHandlerImpl
	implements ReverseSignalHandler
{
	@Inject
	private BridgeConnector connector;

	@Inject
	private SignalHandler signals; // do NOT extend SignalHandlerSupport. We need the SignalHandler shared instance
	
	@Override
	public Signal query(Signal signal, long queryTimeout)
		throws Exception
	{
		Channel channel = connector.getAcceptedChannel();
		if(channel == null)
		{
			throw new NotImplementedYet("Not connected. Channel is null");
		}
		return signals.query(signal, null, channel, queryTimeout);
	}

	@Override
	public Signal query(Signal signal, ChannelFutureListener onWrite, long queryTimeout)
		throws Exception
	{
		Channel channel = connector.getAcceptedChannel();
		return signals.query(signal, onWrite, channel, queryTimeout);
	}

	@Override
	public ChannelFuture deliver(Signal signal)
		throws Exception
	{
		Channel channel = connector.getAcceptedChannel();
		return signals.deliver(signal, channel);
	}
}
