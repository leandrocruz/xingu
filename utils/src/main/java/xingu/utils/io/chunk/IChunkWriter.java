package xingu.utils.io.chunk;

import java.io.Closeable;
import java.io.IOException;

public interface IChunkWriter
	extends Closeable
{
	void write(IChunk chunk)
		throws IOException;
}
