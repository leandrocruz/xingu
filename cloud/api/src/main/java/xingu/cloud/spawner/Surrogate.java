package xingu.cloud.spawner;

import org.jboss.netty.channel.Channel;

import xingu.utils.ip.IPAddress;

public interface Surrogate
{
	String getId();

	String getRegion();

	IPAddress getIp();
	
	Channel getChannel();
	void setChannel(Channel channel);
	
	boolean isAttached();

	boolean isFinished();
	void setFinished(boolean finished);

	boolean waitReady(long timeToWait)
		throws InterruptedException;

	void notifyWaitReady();
}