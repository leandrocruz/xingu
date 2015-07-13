package xingu.utils.io.chunk;

import java.io.File;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;

import xingu.utils.io.chunk.impl.FramedChunkReaderImpl;

public class FramedChunkUtils
{
	public static void print(File file, Writer writer)
		throws Exception
	{
		FramedChunkReader reader = new FramedChunkReaderImpl(file);
		try
		{
			List<FramedChunk> chunks = reader.read();
			System.out.println("There are "+chunks.size()+" chunks");
			int i = 0;
			for(FramedChunk chunk : chunks)
			{
				Frame[] frames = chunk.getFrames();
				writer.write("#" + (++i) + " Chunk ["+frames.length+"]\n");
				for(Frame frame : frames)
				{
					String type = "";
					switch(frame.getType())
					{
						case Frame.IN_MEMORY:
							type = "IN_MEMORY";
							break;

						case Frame.LAZY:
							type = "LAZY";
							break;

						case Frame.MISSING:
							type = "MISSING";
							break;

						default:
							break;
					}
					writer.write("  Frame ["+frame.getSize()+"]: " + type);
					if("IN_MEMORY".equals(type))
					{
						writer.write(" -> " + frame.readString());
					}
					writer.write("\n");
				}
			}
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}
}