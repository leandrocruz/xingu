package xingu.cloud.spawner;

import org.jboss.netty.channel.Channel;

import br.com.ibnetwork.xingu.utils.ip.IPAddress;

public interface Surrogate
{
	String getId();

	String getRegion();

	IPAddress getIp();
	
	Channel getChannel();
	void setChannel(Channel channel);
	
	boolean isAttached();
	
	boolean waitReady(long timeToWait)
		throws InterruptedException;
}