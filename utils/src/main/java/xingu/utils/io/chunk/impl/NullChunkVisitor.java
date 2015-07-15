package xingu.utils.io.chunk.impl;

import xingu.utils.io.chunk.ChunkVisitor;
import xingu.utils.io.chunk.Frame;
import xingu.utils.io.chunk.FramedChunk;

public class NullChunkVisitor
	implements ChunkVisitor
{
	private static final NullChunkVisitor INSTANCE = new NullChunkVisitor();
	
	public static ChunkVisitor instance()
	{
		return INSTANCE;
	}

	private NullChunkVisitor()
	{}

	@Override
	public void onNoCapacity()
	{}

	@Override
	public void onFrameCount(int count)
	{}

	@Override
	public void onSize(int size)
	{}

	@Override
	public void onType(byte type)
	{}

	@Override
	public void onFrame(Frame frame)
	{}

	@Override
	public void onChunk(FramedChunk chunk)
	{}
}
