package xingu.node.console.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import xingu.node.console.RemoteConsole;
import xingu.node.server.impl.ServerNodeSupport;

import org.apache.avalon.framework.activity.Initializable;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class RemoteConsoleImpl
	extends ServerNodeSupport
	implements RemoteConsole, Initializable
{
	private ChannelHandler handler;
	
	@Override
	public void initialize()
		throws Exception
	{
		handler = factory.create(ConsoleChannelHandler.class);
	}

	@Override
	protected ChannelPipelineFactory getChannelPipelineFactory()
	{
		return new ChannelPipelineFactory()
		{
			@Override
			public ChannelPipeline getPipeline()
				throws Exception
			{
				ChannelPipeline pipeline = pipeline();
				pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		};
	}
}
