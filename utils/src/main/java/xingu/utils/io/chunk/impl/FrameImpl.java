package xingu.utils.io.chunk.impl;

import java.io.IOException;

import xingu.lang.NotImplementedYet;
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

	@Override
	public String readString()
		throws IOException
	{
		if(Frame.IN_MEMORY != type)
		{
			throw new NotImplementedYet();
		}
		
		return new String(payload);
	}
}