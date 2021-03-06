package xingu.node.server.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.lang.thread.DaemonThreadFactory;
import xingu.lang.thread.SimpleThreadNamer;
import xingu.netty.protocol.SayHi;
import xingu.node.commons.Node;

public class ServerNodeSupport
	implements Node, Configurable, Startable
{
	@Inject
	protected Factory			factory;

	private String				name;

	private String				address;

	private int					port;

	private ServerBootstrap		bootstrap;

	private Executor			workerExecutor;

	private Executor			bossExecutor;

	protected ChannelGroup		mainChannelGroup	= new DefaultChannelGroup("main");

	protected AddChannelToGroup	channelCollector	= new AddChannelToGroup(mainChannelGroup);

	protected Logger			logger				= LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		address = conf.getAttribute("address", "0.0.0.0");
		port    = conf.getAttributeAsInteger("port", 8899);
		name    = conf.getAttribute("name");
	}

	@Override
	public void start()
		throws Exception
	{
		DaemonThreadFactory workerThreadFactory 
			= new DaemonThreadFactory(new SimpleThreadNamer(name + "Worker"));
		DaemonThreadFactory bossThreadFactory
			= new DaemonThreadFactory(new SimpleThreadNamer(name + "Boss"));
		workerExecutor = Executors.newCachedThreadPool(workerThreadFactory);
		bossExecutor   = Executors.newCachedThreadPool(bossThreadFactory);
		bootstrap      = new ServerBootstrap(new OioServerSocketChannelFactory(bossExecutor, workerExecutor, new ThreadNameDeterminer() {
			@Override
			public String determineThreadName(String currentThreadName, String proposedThreadName)
				throws Exception
			{
				return currentThreadName;
			}
		}));
		ChannelPipelineFactory pipelineFactory = getChannelPipelineFactory();
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.setOption("tcpNoDelay",		true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("reuseAddress", 	true);
		Channel channel = bootstrap.bind(new InetSocketAddress(address, port));
		mainChannelGroup.add(channel);

		logger.info("Node '{}' started at {}:{}", new Object[] { name, address, port });
	}
	
	/*
	 * Called only once
	 */
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
		        pipeline.addLast("encoder", new StringEncoder());
		        pipeline.addLast("decoder", new StringDecoder());
		        pipeline.addLast("handler", new SayHi());
				pipeline.addLast("channel-collector", channelCollector);
		        return pipeline;
			}
		};
	}

	@Override
	public void stop()
		throws Exception
	{
		//System.err.println("SERVER NODE STOP -- START");
		/*
		 * Hack/Ugly. Probably a Netty problem TODO: close all opened channels
		 */
//		Iterator<Channel> it = mainChannelGroup.iterator();
//		while(it.hasNext())
//		{
//			Channel channel = it.next();
//			System.err.println("Closing: " + channel);
//			
//		}
		mainChannelGroup.close().awaitUninterruptibly();

		((ExecutorService) workerExecutor).shutdown();
		((ExecutorService) bossExecutor).shutdown();

		if(bootstrap != null)
		{
			bootstrap.releaseExternalResources();
		}
		//System.err.println("SERVER NODE STOP -- END");
	}
}