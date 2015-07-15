package xingu.utils.io.chunk;

import java.io.File;
import java.io.PrintStream;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;

import xingu.utils.io.chunk.impl.FramedChunkReaderImpl;
import xingu.utils.io.chunk.impl.PrintChunkVisitor;

public class FramedChunkUtils
{
	public static void print(File file, PrintStream printer)
		throws Exception
	{
		FramedChunkReader reader = new FramedChunkReaderImpl(file);
		try
		{
			List<FramedChunk> chunks = reader.read(new PrintChunkVisitor(printer));
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