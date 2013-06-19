package xingu.tunnel.impl;

import org.apache.avalon.framework.activity.Startable;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.client.bridge.BridgeConnector;
import xingu.tunnel.Agent;
import xingu.tunnel.proto.ClientConnected;
import xingu.tunnel.proto.ClientDisconnected;
import xingu.tunnel.proto.ClientMessage;
import xingu.tunnel.proto.TcpMessage;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class NettyAgent
	extends SimpleChannelHandler
	implements Agent, Startable
{
	@Inject
	private BridgeConnector connector;
	
	private Channel	toProxy;

	private Logger	logger	= LoggerFactory.getLogger(getClass());

	@Override
	public void start()
		throws Exception
	{
		Channel channel = connector.connect();
		if(channel == null)
		{
			throw new NotImplementedYet();
		}
		toProxy = channel;
		channel.getPipeline().addLast("handler", this);
	}

	@Override
	public void stop()
		throws Exception
	{
		if(toProxy != null)
		{
			toProxy.close();
			toProxy = null;
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception
	{
		e.getCause().printStackTrace();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Object obj = e.getMessage();
		if(obj instanceof TcpMessage == false)
		{
			throw new NotImplementedYet("Can't handle " + obj);
		}
		
		TcpMessage msg = TcpMessage.class.cast(obj);
		if(msg instanceof ClientConnected)
		{
			whenClientConnected(ClientConnected.class.cast(msg));
		}
		else if(msg instanceof ClientDisconnected)
		{
			whenClientDisconnected(ClientDisconnected.class.cast(msg));
		}
		else
		{
			whenClientMessage(ClientMessage.class.cast(msg));
		}
	}

	private void whenClientMessage(ClientMessage msg)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	private void whenClientDisconnected(ClientDisconnected msg)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	private void whenClientConnected(ClientConnected msg)
		throws Exception
	{
		//TODO: create a new connection to our real server
		throw new NotImplementedYet();
	}
}