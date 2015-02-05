package xingu.utils.io.chunk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChunkReader
{
	private InputStream	is;

	public ChunkReader(InputStream is)
	{
		this.is = is;
	}

	public List<Chunk> read()
		throws Exception
	{
		List<Chunk> result    = new ArrayList<>();
		int         available = is.available();
		while(available > 0)
		{
			int    len  = is.read();
			byte[] data = new byte[len];
			int    read = is.read(data, 0, len);
			result.add(new ChunkImpl(len, read, data));
			available = is.available();
		}
		return result;
	}
}