package xingu.utils.io.chunk.impl;

import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.FramedChunk;

public class FramedChunkImpl
	implements FramedChunk
{
	private int index = 0;
	
	private Frame[] frames;
	
	public FramedChunkImpl(int frameCount)
	{
		this.frames = new Frame[frameCount];
	}

	@Override
	public Frame[] getFrames()
	{
		return frames;
	}

	@Override
	public void add(Frame frame)
	{
		frames[index++] = frame;
	}
}