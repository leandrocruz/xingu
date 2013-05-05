package xingu.node.server.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import org.apache.avalon.framework.activity.Initializable;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import xingu.node.commons.protocol.SecureDecoder;
import xingu.node.commons.protocol.SecureEncoder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public abstract class SecureServerNodeSupport
	extends ServerNode
	implements Initializable
{
    @Inject
    private Factory factory;

    private ChannelHandler encoder;

    @Override
    public void initialize() 
        throws Exception
    {
        encoder = factory.create(SecureEncoder.class);
    }

	@Override
	protected ChannelPipelineFactory getChannelPipelineFactory()
	{
        /*
         * since our decoder can't be used in a concurrent environment we need to create a new instance for every new client
         */
		final ChannelHandler decoder = factory.create(SecureDecoder.class);
		final ChannelHandler handler = messageHandler();
		
		if(handler == null)
		{
			throw new NotImplementedYet("MessageHandler not provided");
		}

		return new ChannelPipelineFactory()
		{
			@Override
			public ChannelPipeline getPipeline()
				throws Exception
			{
		        ChannelPipeline pipeline = pipeline();
				pipeline.addLast("decoder", decoder);
		        pipeline.addLast("encoder", encoder);
		        pipeline.addLast("handler", handler);
		        addChannelCollector(pipeline);
		        return pipeline;
			}
		};
	}
}