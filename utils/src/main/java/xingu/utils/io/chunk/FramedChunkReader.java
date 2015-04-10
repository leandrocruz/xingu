package xingu.utils.io.chunk;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface FramedChunkReader
	extends Closeable
{
	List<FramedChunk> read()
		throws IOException;
}
