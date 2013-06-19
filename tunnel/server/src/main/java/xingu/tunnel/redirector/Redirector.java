package xingu.tunnel.redirector;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public interface Redirector
{
	void onClientConnected(Channel channel);
	void onClientDisconnected(Channel channel);
	void onClientMessage(Channel channel, ChannelBuffer buffer);

	void onAgentConnected(Channel channel);	
	void onAgentDisconnected(Channel channel);
	void onAgentMessage(Channel channel, ChannelBuffer buffer);
}
