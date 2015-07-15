package xingu.utils.io.chunk.impl;

import java.io.IOException;
import java.io.PrintStream;

import xingu.utils.io.chunk.ChunkVisitor;
import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.FramedChunk;
import xingu.utils.io.chunk.FramedChunkUtils;

public class PrintChunkVisitor
	implements ChunkVisitor
{
	private PrintStream	printer;

	private int frames = 0;
	
	private int chunks = 0;
	
	public PrintChunkVisitor(PrintStream printer)
	{
		this.printer = printer;
	}

	@Override
	public void onNoCapacity()
	{}

	@Override
	public void onFrameCount(int count)
	{
		printer.println("["+chunks+"] Starting Chunk ["+count+"]");
	}

	@Override
	public void onSize(int size)
	{
		printer.println("  ["+frames+"] Frame Size: " + size);
	}

	@Override
	public void onType(byte type)
	{
		printer.println("  ["+frames+"] Frame Type: " + FramedChunkUtils.typeName(type));
	}

	@Override
	public void onFrame(Frame frame)
	{
		byte type = frame.getType();
		switch(type)
		{
			case Frame.IN_MEMORY:
			case Frame.LAZY:
				try
				{
					printer.println("  ["+frames+"] Frame Payload: " + frame.readString());
				}
				catch(IOException e)
				{}
				break;

			case Frame.MISSING:
				printer.println("  ["+frames+"] Missing Frame");
				
			default:
				break;
		}
		frames++;
	}

	@Override
	public void onChunk(FramedChunk chunk)
	{
		frames = 0;
		chunks++;
		printer.println("--");
	}
}
