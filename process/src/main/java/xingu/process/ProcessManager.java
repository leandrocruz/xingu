package xingu.process;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

public interface ProcessManager
{
	int exec(List<String> line)
		throws Exception;

	int exec(List<String> line, File baseDir)
		throws Exception;

	int exec(List<String> line, File baseDir, OutputStream os)
		throws Exception;
}
