package xingu.process.impl;

import java.io.File;
import java.io.OutputStream;

import xingu.process.ProcessManager;

public class ProcessManagerImpl
	implements ProcessManager
{
	@Override
	public int exec(String line)
	{
		return ProcessUtils.exec(line, null, System.out);
	}

	@Override
	public int exec(String line, File baseDir)
	{
		return ProcessUtils.exec(line, baseDir, System.out);
	}

	@Override
	public int exec(String line, File baseDir, OutputStream os)
	{
		return ProcessUtils.exec(line, baseDir, os);
	}

}
