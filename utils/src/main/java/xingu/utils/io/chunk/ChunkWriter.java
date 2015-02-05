package xingu.utils.io.chunk;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

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
		byte[] bytes = ByteBuffer.allocate(4).putInt(number).array();
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
		int len = data.length;
		os.write(len);
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