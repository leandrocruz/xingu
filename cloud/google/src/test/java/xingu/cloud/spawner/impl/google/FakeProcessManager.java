package xingu.cloud.spawner.impl.google;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import xingu.process.ProcessManager;

public class FakeProcessManager
	implements ProcessManager
{
	private int		result;

	private String	output;

	private String	error;

	public FakeProcessManager(int result, String output, String error)
	{
		this.result = result;
		this.output = output;
		this.error  = error;
	}
	
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
		return exec(line, null, null, null);
	}

	@Override
	public int exec(List<String> line, File baseDir, File output, File error)
		throws Exception
	{
		if(this.output != null)
		{
			FileUtils.write(output, this.output);
		}
		if(this.error != null)
		{
			FileUtils.write(error, this.error);
		}
		return result;
	}
}
