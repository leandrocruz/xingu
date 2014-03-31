package xingu.node.client.bridge.impl;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.client.bridge.BridgeConnector;
import xingu.node.client.bridge.OnConnect;
import br.com.ibnetwork.xingu.lang.thread.DaemonThreadFactory;
import br.com.ibnetwork.xingu.lang.thread.SimpleThreadNamer;
import br.com.ibnetwork.xingu.utils.StringUtils;
import br.com.ibnetwork.xingu.utils.TimeUtils;

public class BridgeConnectorImpl
	extends SimpleChannelHandler
	implements BridgeConnector, Configurable, Startable
{
	private boolean					useTcpNoDeplay;

	private long					acquirePortTimeout;

	private long					connectionTimeout;

	protected DaemonThreadFactory	threadFactory;

	private ClientBootstrap			bootstrap;

	private String					host;

	private int[]					ports;

	protected int					foundPort	= 0;

	protected Semaphore				sem			= new Semaphore(0);

	protected Logger				logger		= LoggerFactory.getLogger(getClass());

	private Channel					acceptedChannel;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
    	conf = conf.getChild("connection");
        host = conf.getAttribute("address", "127.0.0.1");
        ports = readPorts(conf);
        acquirePortTimeout = TimeUtils.toMillis(conf.getAttribute("acquirePortTimeout","30s")); 
        connectionTimeout = TimeUtils.toMillis(conf.getAttribute("connectionTimeout","10s"));
        useTcpNoDeplay = conf.getAttributeAsBoolean("useTcpNoDeplay", true);
	}

	private int[] readPorts(Configuration conf)
	{
        String ports = conf.getAttribute("ports", "8899");
        String[] availablePorts = ports.split(",");
        int size = availablePorts.length;
        int[] result = new int[size];
        for (int i=0 ; i<size ; i++)
        {
        	String value = availablePorts[i].trim();
        	result[i] = StringUtils.toInt(value, 0); 
        }
		return result;
	}

	@Override
	public void start()
		throws Exception
	{}

	@Override
	public void stop()
		throws Exception
	{
		releaseResources();
	}

	private void releaseResources()
	{
		if(threadFactory != null)
		{
			threadFactory.interruptAllThreads();
			threadFactory = null;
		}
        if(bootstrap != null)
        {
            bootstrap.releaseExternalResources();
            bootstrap = null;
        }
	}

	@Override
	public Channel getAcceptedChannel()
	{
		return acceptedChannel;
	}

	@Override
	public Future<Channel> connect(final OnConnect onConnect)
	{
		Callable<Channel> task = new Callable<Channel>(){

			@Override
			public Channel call()
				throws Exception
			{
				foundPort = 0;
		    	int size = ports.length;
		    	for (int i = 0; i < size; i++)
				{
					int     port    = ports[i];
					Channel channel = tryPort(port);
					if(channel != null)
					{
						BridgeConnectorImpl.this.acceptedChannel = channel;
						onConnect.onSuccess(channel);
						return channel;						
					}
				}
		    	logger.info("No port available from '{}'", ports);
		    	onConnect.onError(null);
		    	return null;
			}
		};
		
		ExecutorService executor = executor();
		return executor.submit(task);
	}

	protected ExecutorService executor()
	{
		DaemonThreadFactory tf = new DaemonThreadFactory(new SimpleThreadNamer("BridgeConnectorWorker"));
		return Executors.newSingleThreadExecutor(tf);
	}

	private Channel tryPort(int port)
	{
		long start = System.currentTimeMillis();
		
		/*
		 * Must 'reset' the semaphore when a bad port releases the lock before a good port is tried
		 */
		sem.drainPermits();
		
		InetSocketAddress address = new InetSocketAddress(host, port);
        logger.info("Connecting client to: '{}:{}'", host, port);

        DaemonThreadFactory threadFactory = new DaemonThreadFactory(new SimpleThreadNamer("BridgeConnectorClientWorker"));
        ClientBootstrap     bootstrap     = bootstrap(threadFactory);
		ChannelFuture       future        = connectTo(bootstrap, address);
		future.awaitUninterruptibly(connectionTimeout);
		Channel channel = future.getChannel();
		try
		{
			if(future.isSuccess() 
					&& sem.tryAcquire(acquirePortTimeout, TimeUnit.MILLISECONDS) 
					&& foundPort > 0)
			{
				/*
				 * We have a good port/channel that can be used to talk to Broker.
				 * Now, we must close other channels
				 */
				logger.info("Server running at " + host + ":" + foundPort);
				this.bootstrap     = bootstrap;
				this.threadFactory = threadFactory;
				return channel;
			}
			else
			{
				long period = System.currentTimeMillis() - start;
				logger.info("Port '{}' discarded after {}", port, TimeUtils.toSeconds(period));
			}
		}
		catch (InterruptedException e)
		{}
		finally
		{
			if(foundPort <= 0)
			{
				clear(channel, bootstrap, threadFactory);
			}
		}
		
		return null;
	}
	
	private void clear(Channel channel, ClientBootstrap bootstrap, DaemonThreadFactory threadFactory)
	{
		if(channel != null)
		{
			channel.close().awaitUninterruptibly();
		}
		threadFactory.interruptAllThreads();
		bootstrap.releaseExternalResources();
	}

	
	private ChannelFuture connectTo(ClientBootstrap bootstrap, final InetSocketAddress address) 
    {
		//final int port = address.getPort();
        ChannelFuture future = bootstrap.connect(address);
        future.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) 
                throws Exception
            {
                if(future.isSuccess())
                {
                	logger.info("Connected to '{}'", address);
                }
                else
                {
                    future.cancel();
                    logger.info("Connection to '{}' failed", address);
                    sem.release();
                }
            }
        });
        return future;
    }
	
    private ClientBootstrap bootstrap(ThreadFactory threadFactory)
    {
        Executor                   executor       = Executors.newSingleThreadExecutor(threadFactory);
        ClientSocketChannelFactory channelFactory = new OioClientSocketChannelFactory(executor);
        ClientBootstrap            bootstrap      = new ClientBootstrap(channelFactory);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", useTcpNoDeplay);
        ChannelPipelineFactory channelPipelineFactory = getChannelPipelineFactory();
		bootstrap.setPipelineFactory(channelPipelineFactory);
        return bootstrap;
    }

    protected ChannelPipelineFactory getChannelPipelineFactory()
	{
    	return new ChannelPipelineFactory()
		{
			public ChannelPipeline getPipeline()
				throws Exception
			{
				ChannelPipeline pipeline = pipeline();
		        pipeline.addLast("encoder", new StringEncoder());
		        pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("bridge-connector",  BridgeConnectorImpl.this);
				return pipeline;
			}
		};
	}

	@Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception
    {
        logger.info("Channel connected '{}'", e.getChannel());
    }
    
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) 
        throws Exception
    {
        logger.info("Channel disconnected '{}'", e.getChannel());
    	InetSocketAddress inet = (InetSocketAddress) e.getChannel().getRemoteAddress();
    	int port = inet.getPort();
        if(foundPort == port)
        {
        	ctx.sendUpstream(e);
        }
    }

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception
	{
		Throwable t = e.getCause();
		if(t instanceof ConnectException)
		{
			/* 
			 * There is no need to handle this exception here since
			 * connectTo() ChannelFuture will release the semaphore
			 */
		}
		else
		{
			logger.warn("Handshake error", e.getCause());
			ctx.sendUpstream(e);
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Object msg = e.getMessage();
		System.err.println("[CONNECTOR] Message Received: " + msg);
		String s = (String) msg;
		if(s.startsWith("Hi"))
		{
			Channel channel = e.getChannel();
        	InetSocketAddress inet = (InetSocketAddress) channel.getRemoteAddress();
        	foundPort = inet.getPort();
        	sem.release();
        	System.err.println("[CONNECTOR] Found port: " + foundPort);
        	
        	channel.write("Ping");
		}
        ctx.sendUpstream(e);
	}
}