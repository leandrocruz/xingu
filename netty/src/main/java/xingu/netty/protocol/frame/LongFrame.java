package xingu.netty.protocol.frame;

import org.jboss.netty.buffer.ChannelBuffer;


public class LongFrame
	extends Frame
{
	private long payload;

	public LongFrame(long data)
	{
		this.payload = data;
	}

	@Override
	protected void writePayload(ChannelBuffer buffer)
	{
		buffer.writeLong(payload);
	}

	@Override
	protected int getSize()
	{
		return 2*INT_LEN;
	}
}