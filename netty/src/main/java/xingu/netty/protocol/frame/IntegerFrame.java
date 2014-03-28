package xingu.netty.protocol.frame;

import org.jboss.netty.buffer.ChannelBuffer;


public class IntegerFrame
	extends Frame
{
	private int payload;

	public IntegerFrame(int data)
	{
		this.payload = data;
	}

	@Override
	protected void writePayload(ChannelBuffer buffer)
	{
		buffer.writeInt(payload);
	}

	@Override
	protected int getSize()
	{
		return INT_LEN;
	}
}