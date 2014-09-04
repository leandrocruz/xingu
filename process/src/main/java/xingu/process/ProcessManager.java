package xingu.process;

import java.io.File;
import java.util.List;

public interface ProcessManager
{
	int exec(List<String> line)
		throws Exception;

	int exec(List<String> line, File baseDir)
		throws Exception;

	int exec(List<String> line, File baseDir, File output, File error)
		throws Exception;
}
