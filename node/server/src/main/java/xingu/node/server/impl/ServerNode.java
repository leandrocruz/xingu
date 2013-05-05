package xingu.node.server.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

public abstract class ServerNode
	extends ServerNodeSupport
{
	@Override
	protected ChannelPipelineFactory getChannelPipelineFactory()
	{
		final ChannelHandler handler = messageHandler();
		return new ChannelPipelineFactory()
		{
			@Override
			public ChannelPipeline getPipeline()
				throws Exception
			{
		        ChannelPipeline pipeline = pipeline();
		        /*
		         * since our decoder can't be used in a concurrent environment we need to create a new instance for every new client
		         */
		        pipeline.addLast("handler", handler);
		        addChannelCollector(pipeline);
		        return pipeline;
			}
		};

	}

	protected abstract ChannelHandler messageHandler();
}
