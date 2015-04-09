package xingu.utils.io.chunk.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.IChunk;
import xingu.utils.io.chunk.IChunkWriter;

public class IChunkWriterImpl
	implements IChunkWriter
{
	private File				file;

	private RandomAccessFile	source;

	public IChunkWriterImpl(File file)
		throws FileNotFoundException
	{
		this.file = file;
		source = new RandomAccessFile(file, "rws");
	}

	@Override
	public void close()
		throws IOException
	{
		source.close();
	}

	@Override
	public void write(IChunk chunk)
		throws IOException
	{
		Frame[] frames = chunk.getFrames();
		source.writeInt(frames.length);

		for(Frame frame : frames)
		{
			byte   type  = frame.getType();
			byte[] bytes = frame.getPayload();
			int    len   = bytes.length;

			source.writeInt(len);
			source.writeByte(type);
			source.write(bytes);
		}
	}

	@Override
	public String toString()
	{
		return "ChunkWriter: " + file;
	}
}