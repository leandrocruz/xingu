package xingu.utils.io.chunk;

public class ChunkImpl
	implements Chunk
{
	private int    size;

	private byte[] bytes;

	private int    read;

	public ChunkImpl(int size, int read, byte[] bytes)
	{
		this.size  = size;
		this.read  = read;
		this.bytes = bytes;
	}

	@Override
	public int getSize()
	{
		return size;
	}

	@Override
	public byte[] getBytes()
	{
		return bytes;
	}

	@Override
	public boolean isTruncated()
	{
		return size != read;
	}

	@Override
	public int getRead()
	{
		return read;
	}

	@Override
	public String toString()
	{
		return "Chunk ["+size+"/"+read+"]";
	}
}