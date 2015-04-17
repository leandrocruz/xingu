package xingu.utils.io.chunk.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

import xingu.utils.io.chunk.Frame;

public class LazyFrame
	implements Frame
{
	private int					size;

	private byte				type;

	private long				pos;

	private RandomAccessFile	source;

	private String				path;

	public LazyFrame(byte type, int size, long pos, RandomAccessFile source, String path)
	{
		this.size   = size;
		this.type   = type;
		this.pos    = pos;
		this.source = source;
		this.path   = path;
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
	
	@Override
	public String readString()
		throws IOException
	{
		byte[] payload = getPayload();
		return new String(payload);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof LazyFrame))
		{
			return false;
		}
		
		LazyFrame other = (LazyFrame) obj;
		return Objects.equals(size, other.size)
				&& Objects.equals(type, other.type)
				&& Objects.equals(pos, other.pos)
				&& Objects.equals(path, other.path);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(size, type, pos, path);
	}
	
}