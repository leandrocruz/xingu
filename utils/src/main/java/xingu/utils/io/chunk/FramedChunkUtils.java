package xingu.utils.io.chunk;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FramedChunkUtils
{
	public static void print(FramedChunkReader reader, ChunkVisitor visitor, PrintStream printer)
		throws Exception
	{
		try
		{
			List<FramedChunk> chunks = reader.read(visitor);
			printer.println("There are "+chunks.size()+" chunks");
			int i = 0;
			for(FramedChunk chunk : chunks)
			{
				Frame[] frames = chunk.getFrames();
				printer.println("#" + (++i) + " Chunk ["+frames.length+"]");
				for(Frame frame : frames)
				{
					String type = typeName(frame.getType());
					printer.print("  Frame ["+frame.getSize()+"]: " + type);
					if("IN_MEMORY".equals(type))
					{
						printer.print(" -> " + frame.readString());
					}
					printer.print("\n");
				}
			}
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}

	public static String typeName(byte type)
	{
		switch(type)
		{
			case Frame.IN_MEMORY:
				return "IN_MEMORY";

			case Frame.LAZY:
				return "LAZY";

			case Frame.MISSING:
				return "MISSING";
		}
		return "NA";
	}
}