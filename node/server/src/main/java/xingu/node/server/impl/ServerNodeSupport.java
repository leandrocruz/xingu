package xingu.node.server.impl;

import static org.jboss.netty.channel.ChannelState.CONNECTED;

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
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.thread.DaemonThreadFactory;
import br.com.ibnetwork.xingu.lang.thread.SimpleThreadNamer;
import xingu.node.server.Node;

public abstract class ServerNodeSupport
	implements Node, Configurable, Startable
{
	@Inject
	protected Factory		factory;

	private String			name;

	private String			address;

	private int				port;

	private ServerBootstrap	bootstrap;

	private Executor		workerExecutor;

	private Executor		bossExecutor;

	protected ChannelGroup	channels	= new DefaultChannelGroup("main");

	protected Logger		logger		= LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		address = conf.getAttribute("address", "0.0.0.0");
		port = conf.getAttributeAsInteger("port", 8899);
		name = conf.getAttribute("name");
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
		bossExecutor = Executors.newCachedThreadPool(bossThreadFactory);
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(workerExecutor, bossExecutor));
		ChannelPipelineFactory pipelineFactory = getChannelPipelineFactory();
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.setOption("tcpNoDelay",		true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("reuseAddress", 	true);
		Channel channel = bootstrap.bind(new InetSocketAddress(address, port));
		addChannelToMainGroup(channel);

		logger.info("Node '{}' started at {}:{}", new Object[] { name, address, port });
	}

	private void addChannelToMainGroup(Channel channel)
	{
		/*
		 * add this channel to channels so we can shut it down latter
		 */
		channels.add(channel);
	}

	protected void addChannelCollector(ChannelPipeline pipeline)
	{
		pipeline.addLast("channel-collector", new ChannelUpstreamHandler()
		{
			@Override
			public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
				throws Exception
			{
				if(e instanceof ChannelStateEvent)
				{
					ChannelStateEvent evt = (ChannelStateEvent) e;
					Object value = evt.getValue();
					if(evt.getState() == CONNECTED && value != null)
					{
						addChannelToMainGroup(evt.getChannel());
					}
				}
			}
		});
	}
	
	protected abstract ChannelPipelineFactory getChannelPipelineFactory();

	@Override
	public void stop()
		throws Exception
	{
		/*
		 * Hack/Ugly. Probably a Netty problem TODO: close all opened channels
		 */

		channels.close().awaitUninterruptibly();

		((ExecutorService) workerExecutor).shutdown();
		((ExecutorService) bossExecutor).shutdown();

		if(bootstrap != null)
		{
			bootstrap.releaseExternalResources();
		}
	}
}