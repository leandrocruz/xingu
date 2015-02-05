package xingu.utils.io.chunk;

public interface Chunk
{
	boolean isTruncated();

	int getSize();
	
	int getRead();
	
	byte[] getBytes();
}
