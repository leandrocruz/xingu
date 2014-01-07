package xingu.process.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import xingu.process.ProcessManager;

public class ProcessManagerImpl
	implements ProcessManager
{
	@Override
	public int exec(List<String> line)
	{
		return ProcessUtils.exec(StringUtils.join(line, " "), null, System.out);
	}

	@Override
	public int exec(List<String> line, File baseDir)
	{
		return ProcessUtils.exec(StringUtils.join(line, " "), baseDir, System.out);
	}

	@Override
	public int exec(List<String> line, File baseDir, OutputStream os)
	{
		return ProcessUtils.exec(StringUtils.join(line, " "), baseDir, os);
	}

}
