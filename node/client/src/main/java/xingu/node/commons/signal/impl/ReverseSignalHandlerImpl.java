package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.node.client.bridge.BridgeConnector;
import xingu.node.commons.signal.ReverseSignalHandler;
import xingu.node.commons.signal.Signal;
import br.com.ibnetwork.xingu.container.Inject;

public class ReverseSignalHandlerImpl
	extends SignalHandlerSupport
	implements ReverseSignalHandler
{
	@Inject
	private BridgeConnector connector;

	@Override
	public Signal query(Signal signal)
		throws Exception
	{
		Channel channel = connector.getAcceptedChannel();
		return query(signal, null, channel);
	}

	@Override
	public Signal query(Signal signal, ChannelFutureListener onWrite)
		throws Exception
	{
		Channel channel = connector.getAcceptedChannel();
		return query(signal, onWrite, channel);
	}

	@Override
	public ChannelFuture deliver(Signal signal)
		throws Exception
	{
		Channel channel = connector.getAcceptedChannel();
		return deliver(signal, channel);
	}
}
