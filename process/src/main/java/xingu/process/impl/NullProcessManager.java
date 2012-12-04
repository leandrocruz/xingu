package xingu.process.impl;

import java.io.File;
import java.io.OutputStream;

import xingu.process.ProcessManager;

public class NullProcessManager
	implements ProcessManager
{
	@Override
	public int exec(String line)
	{
		return exec(line, null);
	}

	@Override
	public int exec(String line, File baseDir)
	{
		return exec(line, null, null);
	}

	@Override
	public int exec(String line, File baseDir, OutputStream os)
	{
		if(baseDir != null)
		{
			System.err.println("Executing '" + line + "' from '" + baseDir + "'");
		}
		else
		{
			System.err.println("Executing '" + line + "'");
		}
		return 0;
	}
}
