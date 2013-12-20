package xingu.process;

import java.io.File;
import java.io.OutputStream;

public interface ProcessManager
{
	int exec(String line)
		throws Exception;

	int exec(String line, File baseDir)
		throws Exception;

	int exec(String line, File baseDir, OutputStream os)
		throws Exception;
}
