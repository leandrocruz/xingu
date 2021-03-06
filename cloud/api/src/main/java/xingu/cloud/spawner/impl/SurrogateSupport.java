package xingu.cloud.spawner.impl;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;

import xingu.cloud.spawner.Surrogate;
import xingu.utils.ip.IPAddress;
import xingu.utils.ip.IPUtils;

public class SurrogateSupport
	implements Surrogate
{
	protected final String		id;

	protected String			region;

	protected IPAddress			ip;
	
	protected transient Channel	channel;

	protected boolean			finished;

	protected String			waitStatus	= "pre-wait";

	public SurrogateSupport(String id, String region)
	{
		this.id     = id;
		this.region = region;
	}

	@Override public String getId(){return id;}
	@Override public IPAddress getIp(){return ip;}
	@Override public String getRegion(){return region;}
	@Override public boolean isFinished(){return finished;}
	@Override public void setFinished(boolean finished){this.finished = finished;}
	@Override public synchronized Channel getChannel(){return channel;}

	/* Don't remove. Required by the GUI */
	public String getWaitStatus(){return waitStatus;}
	public void setWaitStatus(String waitStatus){this.waitStatus = waitStatus;}

	@Override
	public synchronized boolean isAttached()
	{
		return channel != null;
	}

	@Override
	public void setChannel(Channel channel)
	{
		this.channel           = channel;
		InetSocketAddress inet = (InetSocketAddress) channel.getRemoteAddress();
		String            addr = inet.getAddress().getHostAddress();
		ip                     = IPUtils.buildIPv4From(addr);
		notifyWaitReady();
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
		waitStatus = "waiting";
		wait(timeToWait);
		waitStatus = "after-wait";
		return isAttached();
	}

	@Override
	public String toString()
	{
		return "Surrogate #" + id + (ip == null ? " not attached" : " at " + ip.getAddress());
	}

	@Override
	public synchronized void notifyWaitReady()
	{
		notify();
	}
}