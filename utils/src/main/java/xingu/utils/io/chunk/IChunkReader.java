package xingu.utils.io.chunk;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface IChunkReader
	extends Closeable
{
	List<IChunk> read()
		throws IOException;
}
