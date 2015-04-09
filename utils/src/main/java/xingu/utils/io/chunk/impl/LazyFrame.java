package xingu.utils.io.chunk.impl;

import java.io.IOException;
import java.io.RandomAccessFile;

import xingu.utils.io.chunk.Frame;

public class LazyFrame
	implements Frame
{
	private int					size;

	private byte				type;

	private long				pos;

	private RandomAccessFile	source;

	public LazyFrame(byte type, int size, long pos, RandomAccessFile source)
	{
		this.size   = size;
		this.type   = type;
		this.pos    = pos;
		this.source = source;
	}

	@Override
	public byte getType()
	{
		return type;
	}

	@Override
	public byte[] getPayload()
		throws IOException
	{
		long filePointer = source.getFilePointer();
		source.seek(pos);

		byte[] payload = new byte[size];
		source.read(payload);
		source.seek(filePointer);

		/*
		 * Don't cache the result
		 */
		return payload;
	}
}