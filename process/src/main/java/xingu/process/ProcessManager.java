package xingu.process;

import java.io.File;
import java.io.OutputStream;

public interface ProcessManager
{
	int exec(String line);

	int exec(String line, File baseDir);

	int exec(String line, File baseDir, OutputStream os);
}
