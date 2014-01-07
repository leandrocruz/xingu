package xingu.process.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import xingu.process.ProcessManager;

public class SimpleProcessManager
	implements ProcessManager
{

	@Override
	public int exec(List<String> line)
		throws Exception
	{
		return exec(line, null);
	}

	@Override
	public int exec(List<String> line, File baseDir)
		throws Exception
	{
		return exec(line, baseDir, System.out);
	}

	@Override
	public int exec(List<String> line, File baseDir, OutputStream os)
		throws Exception
	{
		ProcessBuilder pb = new ProcessBuilder(line);
		if(baseDir != null)
		{
			pb.directory(baseDir);
		}
		
		pb.inheritIO();
		//pb.redirectOutput();
		//pb.redirectError();
		Process p = pb.start();
		return p.waitFor();
	}
}
