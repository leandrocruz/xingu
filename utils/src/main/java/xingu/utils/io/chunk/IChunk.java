package xingu.utils.io.chunk;

public interface IChunk
{
	Frame[] getFrames();

	void add(Frame frame);
}
