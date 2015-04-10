package xingu.utils.io.chunk;

public interface FramedChunk
{
	Frame[] getFrames();

	void add(Frame frame);
}
