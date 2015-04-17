package xingu.utils.io.chunk.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

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
		this(Frame.IN_MEMORY, data == null ? new byte[0] : data.getBytes());
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

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof FrameImpl))
		{
			return false;
		}
		
		FrameImpl other = (FrameImpl) obj;
		return Objects.equals(type, other.type)
				&& Arrays.equals(payload, other.payload);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, payload);
	}
}