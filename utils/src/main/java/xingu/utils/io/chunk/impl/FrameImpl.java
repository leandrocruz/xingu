package xingu.utils.io.chunk.impl;

import xingu.utils.ByteUtils;
import xingu.utils.io.chunk.Frame;

public class FrameImpl
	implements Frame
{
	private byte	type;

	private byte[]	payload;

	public FrameImpl(byte type, byte[] payload)
	{
		this.type    = type;
		this.payload = payload;
	}

	public FrameImpl(String data)
	{
		this(Frame.IN_MEMORY, data.getBytes());
	}

	public FrameImpl(int data)
	{
		this(Frame.IN_MEMORY, ByteUtils.toByteArray(data));
	}

	@Override
	public byte getType()
	{
		return type;
	}

	@Override
	public byte[] getPayload()
	{
		return payload;
	}
}