package xingu.utils.io.chunk.impl;

import xingu.utils.ByteUtils;
import xingu.utils.io.chunk.ByteBag;

public class ByteArrayByteBag
	implements ByteBag
{
	private final byte[] bytes;

	public ByteArrayByteBag(long number)
	{
		this(ByteUtils.toByteArray(number));
	}

	public ByteArrayByteBag(int number)
	{
		this(ByteUtils.toByteArray(number));
	}

	public ByteArrayByteBag(String s)
	{
		this(s == null ? null : s.getBytes());
	}

	public ByteArrayByteBag(byte[] bytes)
	{
		this.bytes = bytes;
	}

	@Override
	public byte[] getBytes()
	{
		return bytes;
	}
}
