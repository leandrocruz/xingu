package xingu.tunnel.proto;

import org.jboss.netty.buffer.ChannelBuffer;

public interface TcpMessage
{
	int getId();
	
	ChannelBuffer getBuffer();
}
