package xingu.utils.io.chunk;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import xingu.utils.ByteUtils;

public class ChunkWriter
	implements Closeable
{
	private OutputStream	os;

	public ChunkWriter(OutputStream os)
	{
		this.os = os;
	}

	public void write(int number)
		throws IOException
	{
		byte[] bytes = ByteUtils.toByteArray(number);
		write(bytes);
	}

	public void write(String string)
		throws IOException
	{
		write(string.getBytes());
	}

	public void write(byte[] data)
		throws IOException
	{
		int    len    = data.length;
		byte[] prolog = ByteUtils.toByteArray(len);
		os.write(prolog);
		os.write(data);
	}

	public void close()
		throws IOException
	{
		os.close();
	}

	public void flush()
		throws IOException
	{
		os.flush();
	}
}