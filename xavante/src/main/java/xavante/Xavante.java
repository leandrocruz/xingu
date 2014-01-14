package xavante;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.server.impl.ServerNodeSupport;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class Xavante
	extends ServerNodeSupport
{
	@Inject
	private Factory	 factory;
	
	public static final Logger logger = LoggerFactory.getLogger("xavante");
	
	public static final String SLASH = "/";

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
		        pipeline.addLast("decoder",		new HttpRequestDecoder());
		        pipeline.addLast("aggregator",	new HttpChunkAggregator(65536));
		        pipeline.addLast("encoder",		new HttpResponseEncoder());
		        pipeline.addLast("handler", 	factory.create(XavanteChannelHandler.class));
				pipeline.addLast("channel-collector", channelCollector);
		        return pipeline;
			}
		};
	}

	public static boolean isRoot(String uri)
	{
		return StringUtils.EMPTY.equals(uri) || SLASH.equals(uri);
	}
}
