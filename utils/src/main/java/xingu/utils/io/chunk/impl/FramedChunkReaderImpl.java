package xingu.utils.io.chunk.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.FramedChunk;
import xingu.utils.io.chunk.FramedChunkReader;

public class FramedChunkReaderImpl
	implements FramedChunkReader
{
	private File				file;

	private RandomAccessFile	source;
	
	public FramedChunkReaderImpl(File file)
		throws FileNotFoundException
	{
		this.file = file;
		source = new RandomAccessFile(file, "r");
	}

	@Override
	public void close()
		throws IOException
	{
		source.close();
	}

	@Override
	public List<FramedChunk> read()
		throws IOException
	{
		List<FramedChunk> result = new ArrayList<>();
		FramedChunk chunk;
		while((chunk = readChunk()) != null)
		{
			result.add(chunk);
		}
		return result;
	}

	private FramedChunk readChunk()
		throws IOException
	{
		if(!hasCapacity(8)) /* read at least the chunk count and  the first frame size */
		{
			return null;
		}

		int    count = source.readInt();
		FramedChunk chunk = new FramedChunkImpl(count);
		for(int i = 0 ; i < count ; i++)
		{
			int size = source.readInt();
			if(hasCapacity(size + 1)) /* payload + type */
			{
				byte type = source.readByte();
				if(type == Frame.IN_MEMORY)
				{
					byte[] payload = new byte[size];
					source.read(payload);
					Frame frame = new FrameImpl(type, payload);
					chunk.add(frame);
				}
				else
				{
					long pos = source.getFilePointer();
					source.seek(pos + size);
					chunk.add(new LazyFrame(type, size, pos, source, file.getAbsolutePath()));
				}
			}
			else
			{
				chunk.add(new MissingFrame(size));
			}
		}
		return chunk;
	}

	private boolean hasCapacity(int len)
		throws IOException
	{
		long pos   = source.getFilePointer();
		long total = source.length();
		return pos + len <= total;
	}

	@Override
	public String toString()
	{
		return "ChunkReader: " + file;
	}
}