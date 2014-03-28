package xingu.netty.protocol.frame;

import org.jboss.netty.buffer.ChannelBuffer;


public class StringFrame
	extends Frame
{
	private String payload;

	public StringFrame(String data)
	{
		this.payload = data;
	}

	@Override
	protected void writePayload(ChannelBuffer buffer)
	{
		buffer.writeBytes(payload.getBytes());
	}

	@Override
	protected int getSize()
	{
		return payload == null ? -1 : payload.length();
	}
}