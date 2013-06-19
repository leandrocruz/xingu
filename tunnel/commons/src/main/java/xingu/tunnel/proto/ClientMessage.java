package xingu.tunnel.proto;

import org.jboss.netty.buffer.ChannelBuffer;

public class ClientMessage
	extends TcpMessageSupport
{
	public ClientMessage()
	{}

	public ClientMessage(int id, ChannelBuffer buffer)
	{
		super(id, buffer);
	}
}