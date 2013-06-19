package xingu.tunnel.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import xingu.node.commons.protocol.PojoDecoder;
import xingu.node.commons.protocol.PojoEncoder;
import xingu.node.server.impl.ServerNodeSupport;
import xingu.tunnel.AgentAcceptor;

public class NettyAgentAcceptor
	extends ServerNodeSupport
	implements AgentAcceptor
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
		        pipeline.addLast("encoder", factory.create(PojoEncoder.class));
		        pipeline.addLast("decoder", factory.create(PojoDecoder.class));
		        pipeline.addLast("handler", factory.create(AgentAcceptorChannelHandler.class));
				pipeline.addLast("channel-collector", channelCollector);
		        return pipeline;
			}
		};
	}
}
