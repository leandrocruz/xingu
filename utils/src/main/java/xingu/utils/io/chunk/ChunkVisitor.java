package xingu.utils.io.chunk;

public interface ChunkVisitor
{
	void onNoCapacity();

	void onFrameCount(int count);

	void onSize(int size);

	void onType(byte type);

	void onFrame(Frame frame);

	void onChunk(FramedChunk chunk);

	void onJump(long pointer, long jump);
}
