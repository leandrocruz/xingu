package xingu.process.impl;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class ProcessUtils
{
	public static int exec(String line)
	{
		return exec(line, null, System.out);
	}

	public static int exec(String line, File baseDir)
	{
		return exec(line, baseDir, System.out);
	}

	public static int exec(String line, File baseDir, OutputStream os)
	{
		CommandLine cmd = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(os));
		if(baseDir != null)
		{
			executor.setWorkingDirectory(baseDir);
		}
		try
		{
			return executor.execute(cmd);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error executing command", e);
		}
	}

}
