package xingu.utils.io.chunk;

import java.io.Closeable;
import java.io.IOException;

public interface FramedChunkWriter
	extends Closeable
{
	void write(FramedChunk chunk)
		throws IOException;
}
