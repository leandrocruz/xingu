package xingu.utils.io.chunk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import xingu.utils.ByteUtils;

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
			byte[] prolog = new byte[4];
			is.read(prolog, 0, 4);
			int len = ByteUtils.toInt(prolog);
			
			byte[] data = new byte[len];
			int    read = is.read(data, 0, len);
			
			result.add(new ChunkImpl(len, read, data));
			available = is.available();
		}
		return result;
	}
}