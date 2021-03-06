package xingu.process.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import xingu.process.ProcessManager;

public class NullProcessManager
	implements ProcessManager
{
	@Override
	public int exec(List<String> line)
	{
		return exec(line, null);
	}

	@Override
	public int exec(List<String> line, File baseDir)
	{
		return exec(line, null, null, null);
	}

	@Override
	public int exec(List<String> line, File baseDir, File output, File error)
	{
		if(baseDir != null)
		{
			System.err.println("Executing '" + StringUtils.join(line, " ") + "' from '" + baseDir + "'");
		}
		else
		{
			System.err.println("Executing '" + StringUtils.join(line, " ") + "'");
		}
		return 0;
	}
}
