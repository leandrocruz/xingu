package xingu.cloud.spawner.impl;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;

import xingu.cloud.spawner.Surrogate;
import br.com.ibnetwork.xingu.utils.ip.IPAddress;
import br.com.ibnetwork.xingu.utils.ip.IPUtils;

public class SurrogateSupport
	implements Surrogate
{
	protected final String		id;

	protected transient Channel	channel;

	protected IPAddress			ip;

	public SurrogateSupport(String id)
	{
		this.id = id;
	}

	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public IPAddress getIp()
	{
		return ip;
	}

	@Override
	public synchronized Channel getChannel()
	{
		return channel;
	}

	@Override
	public synchronized boolean isAttached()
	{
		return channel != null;
	}

	@Override
	public String toString()
	{
		return "Surrogate #" + id + (ip == null ? " not attached" : " at " + ip.getAddress());
	}

	@Override
	public synchronized void setChannel(Channel channel)
	{
		this.channel           = channel;
		InetSocketAddress inet = (InetSocketAddress) channel.getRemoteAddress();
		String            addr = inet.getAddress().getHostAddress();
		ip                     = IPUtils.buildIPv4From(addr);
		notify();
	}

	@Override
	public synchronized boolean waitReady(long timeToWait)
		throws InterruptedException
	{
		boolean attached = isAttached();
		if(attached)
		{
			return true;
		}
		wait(timeToWait);
		return isAttached();
	}
}