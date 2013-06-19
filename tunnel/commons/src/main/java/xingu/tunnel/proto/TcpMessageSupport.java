package xingu.tunnel.proto;

import org.jboss.netty.buffer.ChannelBuffer;

public class TcpMessageSupport
	implements TcpMessage
{
	private int				id;

	private ChannelBuffer	buffer;
	
	public TcpMessageSupport()
	{}
	
	public TcpMessageSupport(int id, ChannelBuffer buffer)
	{
		this.id     = id;
		this.buffer = buffer;	
	}
	
	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public ChannelBuffer getBuffer()
	{
		return buffer;
	}
}
