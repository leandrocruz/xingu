package xingu.process.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import xingu.process.ProcessManager;

public class OldProcessManagerImpl
	implements ProcessManager
{
	@Override
	public int exec(List<String> line)
	{
		return ProcessUtils.exec(StringUtils.join(line, " "), null, null);
	}

	@Override
	public int exec(List<String> line, File baseDir)
	{
		return ProcessUtils.exec(StringUtils.join(line, " "), baseDir, null);
	}

	@Override
	public int exec(List<String> line, File baseDir, File output, File error)
		throws Exception
	{
		OutputStream os = new FileOutputStream(output);
		return ProcessUtils.exec(StringUtils.join(line, " "), baseDir, os);
	}
}
