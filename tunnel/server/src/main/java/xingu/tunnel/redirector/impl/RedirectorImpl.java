package xingu.tunnel.redirector.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.tunnel.proto.ClientConnected;
import xingu.tunnel.proto.ClientDisconnected;
import xingu.tunnel.proto.ClientMessage;
import xingu.tunnel.redirector.Redirector;

public class RedirectorImpl
	implements Redirector
{
	private Channel				toServer;

	private AtomicInteger		count	= new AtomicInteger(0);

	private List<ClientProxy>	clients	= new ArrayList<ClientProxy>();

	private Logger				logger	= LoggerFactory.getLogger(getClass());

	@Override
	public void onClientConnected(Channel channel)
	{
		int         id     = count.incrementAndGet();
		ClientProxy client = new ClientProxy(id, channel);
		clients.add(client);
		logger.info("Client #{} Connected '{}'", id, channel.getRemoteAddress());

		if(toServer == null)
		{
			/*
			 * No Agent/Server connected. Abort
			 */
			channel.close();
		}
		else
		{
			/*
			 * Create a new 'client' connection using our agent
			 */
			toServer.write(new ClientConnected(id));
		}
	}

	@Override
	public void onClientDisconnected(Channel channel)
	{
		ClientProxy client = by(channel);
		int         id     = client.getId();
		clients.remove(client);
		logger.info("Client #{} Disconnected", id);
		
		if(toServer != null)
		{
			toServer.write(new ClientDisconnected(id));
		}
	}

	@Override
	public void onClientMessage(Channel channel, ChannelBuffer buffer)
	{
		ClientProxy client = by(channel);
		int         id     = client.getId();
		logger.info("Client #{} incoming message: {} bytes", id, buffer.readableBytes());
		toServer.write(new ClientMessage(id, buffer));
	}

	@Override
	public void onAgentConnected(Channel channel)
	{
		toServer = channel;
	}

	@Override
	public void onAgentDisconnected(Channel channel)
	{
		System.err.println("TODO: onAgentDisconnected()");
	}

	@Override
	public void onAgentMessage(Channel channel, ChannelBuffer buffer)
	{}

	private ClientProxy by(Channel channel)
	{
		Integer wanted = channel.getId();
		for(ClientProxy client : clients)
		{
			Channel c = client.getChannel();
			if(c.getId() == wanted)
			{
				return client;
			}
		}
		return null;
	}
}

/*
 * This class represents an application client
 */
class ClientProxy
{
	private final int		id;

	private final Channel	channel;

	public ClientProxy(int id, Channel channel)
	{
		this.id      = id;
		this.channel = channel;
	}

	public int getId()
	{
		return id;
	}

	public Channel getChannel()
	{
		return channel;
	}
}