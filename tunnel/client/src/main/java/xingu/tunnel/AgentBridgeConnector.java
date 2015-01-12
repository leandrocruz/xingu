package xingu.tunnel;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.MessageEvent;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.node.client.bridge.impl.BridgeConnectorImpl;
import xingu.node.commons.protocol.PojoDecoder;
import xingu.node.commons.protocol.PojoEncoder;
import xingu.utils.NettyUtils;

public class AgentBridgeConnector
	extends BridgeConnectorImpl
{
	@Inject
	private Factory	factory;

	@Override
	protected ChannelPipelineFactory getChannelPipelineFactory()
	{
    	return new ChannelPipelineFactory()
		{
			public ChannelPipeline getPipeline()
				throws Exception
			{
				ChannelPipeline pipeline = pipeline();
		        pipeline.addLast("encoder", factory.create(PojoEncoder.class));
		        pipeline.addLast("decoder", factory.create(PojoDecoder.class));
				pipeline.addLast("bridge-connector",  AgentBridgeConnector.this);
				return pipeline;
			}
		};
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		Object  msg     = e.getMessage();
		if(msg instanceof String)
		{
			String m = (String) msg;
			if("OK".equals(m))
			{
				InetSocketAddress inet = (InetSocketAddress) channel.getRemoteAddress();
				foundPort              = inet.getPort();
				sem.release();
				NettyUtils.removeOnce(channel.getPipeline(), "bridge-connector");
			}
		}
	}
}
