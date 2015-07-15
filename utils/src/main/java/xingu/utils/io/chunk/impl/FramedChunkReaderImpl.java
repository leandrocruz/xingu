package xingu.utils.io.chunk.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import xingu.lang.NotImplementedYet;
import xingu.utils.HexUtils;
import xingu.utils.io.chunk.ChunkVisitor;
import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.FramedChunk;
import xingu.utils.io.chunk.FramedChunkReader;

public class FramedChunkReaderImpl
	implements FramedChunkReader
{
	private File				file;

	protected RandomAccessFile	source;
	
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
		return read(null);
	}
	@Override
	public List<FramedChunk> read(ChunkVisitor visitor)
		throws IOException
	{
		if(visitor == null)
		{
			visitor = NullChunkVisitor.instance();
		}
		List<FramedChunk> result = new ArrayList<>();
		FramedChunk chunk;
		while((chunk = readChunk(visitor)) != null)
		{
			visitor.onChunk(chunk);
			result.add(chunk);
		}
		return result;
	}

	protected long seek(long start)
		throws IOException
	{
		return -1;
	}
	
	protected boolean seekBytes(int count)
	{
		 return count > 10; /* security check for now */
	}
	
	private FramedChunk readChunk(ChunkVisitor visitor)
		throws IOException
	{
		if(!hasCapacity(8)) /* read at least the chunk count and  the first frame size */
		{
			visitor.onNoCapacity();
			return null;
		}

		int count = source.readInt();
		visitor.onFrameCount(count);
		if(seekBytes(count))
		{
			long pointer = source.getFilePointer() - 4; /* roll back an int */
			long jump    = seek(pointer);
			if(jump <= 0)
			{
				throw new NotImplementedYet("Frame count is too large: " + count + " [0x"+HexUtils.toHex(count)+"] at: " + pointer + " [0x"+HexUtils.toHex(pointer)+"]");
			}
			source.seek(jump);
			visitor.onJump(pointer, jump);
			return readChunk(visitor);
		}
		FramedChunk chunk = new FramedChunkImpl(count);
		for(int i = 0 ; i < count ; i++)
		{
			int size = source.readInt();
			visitor.onSize(size);
			if(hasCapacity(size + 1)) /* payload + type */
			{
				byte type = source.readByte();
				visitor.onType(type);
				if(type == Frame.IN_MEMORY)
				{
					byte[] payload = new byte[size];
					source.read(payload);
					Frame frame = new FrameImpl(type, payload);
					visitor.onFrame(frame);
					chunk.add(frame);
				}
				else
				{
					long pos = source.getFilePointer();
					source.seek(pos + size);
					LazyFrame frame = new LazyFrame(type, size, pos, source, file.getAbsolutePath());
					visitor.onFrame(frame);
					chunk.add(frame);
				}
			}
			else
			{
				MissingFrame frame = new MissingFrame(size);
				visitor.onFrame(frame);
				chunk.add(frame);
			}
		}
		return chunk;
	}

	protected boolean hasCapacity(int len)
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
	
	public static void main(String[] args)
		throws Exception
	{
		String file = args[0];
		File f = new File(file);
		if(!f.exists())
		{
			throw new NotImplementedYet("Missing: " + f);
		}
		
		List<FramedChunk> chunks = new FramedChunkReaderImpl(f).read();
		int count = 1;
		for(FramedChunk chunk : chunks)
		{
			System.out.println("Frame: " + (count++));
			Frame[] frames = chunk.getFrames();
			int len = frames.length;
			for(int i = 0; i < len; i++)
			{
				Frame frame = frames[i];
				byte[] payload = frame.getPayload();
				System.out.println("  ["+i+"] " + frame.getType() + " " + new String(payload));
			}
		}
	}
}