package xingu.utils.io.chunk.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.FramedChunk;
import xingu.utils.io.chunk.FramedChunkWriter;

public class FramedChunkWriterImpl
	implements FramedChunkWriter
{
	private File				file;

	private RandomAccessFile	source;

	public FramedChunkWriterImpl(File file)
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
	public synchronized void write(FramedChunk chunk)
		throws IOException
	{
		Frame[] frames = chunk.getFrames();
		source.writeInt(frames.length);

		for(Frame frame : frames)
		{
			byte   type  = frame.getType();
			byte[] bytes = frame.getPayload();
			int    len   = bytes == null ? 0 : bytes.length;

			source.writeInt(len);
			source.writeByte(type);
			if(bytes != null)
			{
				source.write(bytes);
			}
		}
	}

	@Override
	public String toString()
	{
		return "ChunkWriter: " + file;
	}
}