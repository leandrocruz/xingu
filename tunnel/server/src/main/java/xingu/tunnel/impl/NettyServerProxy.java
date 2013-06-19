package xingu.tunnel.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import xingu.node.server.impl.ServerNodeSupport;
import xingu.tunnel.ServerProxy;

/**
 * This is the fake server.
 * Application clients will talk to this server which acts as a proxy to the original server
 */
public class NettyServerProxy
	extends ServerNodeSupport
	implements ServerProxy
{
	@Override
	protected ChannelPipelineFactory getChannelPipelineFactory()
	{
		return new ChannelPipelineFactory()
		{
			/*
			 * Called every time a new connection is made
			 */
			@Override
			public ChannelPipeline getPipeline()
				throws Exception
			{
		        ChannelPipeline pipeline = pipeline();
		        pipeline.addLast("handler", factory.create(ServerProxyChannelHandler.class));
				pipeline.addLast("channel-collector", channelCollector);
		        return pipeline;
			}
		};
	}
}
