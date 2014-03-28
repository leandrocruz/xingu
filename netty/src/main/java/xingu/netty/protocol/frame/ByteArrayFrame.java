package xingu.netty.protocol.frame;

import org.jboss.netty.buffer.ChannelBuffer;


public class ByteArrayFrame
	extends Frame
{
	private byte[]	payload;

	public ByteArrayFrame(byte[] data)
	{
		this.payload = data;
	}

	@Override
	protected void writePayload(ChannelBuffer buffer)
	{
		buffer.writeBytes(payload);
	}

	@Override
	protected int getSize()
	{
		return payload == null ? -1 : payload.length;
	}
}