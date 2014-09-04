package xingu.process.impl;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.process.ProcessManager;

public class ProcessManagerImpl
	implements ProcessManager, Configurable
{
	private boolean	redirect;
	
	private boolean	append;

	private File	stdOut;

	private File	stdErr;

	private String	stdFile	= "xingu-process-manager.log";

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		conf = conf.getChild("log");
		this.redirect = conf.getAttributeAsBoolean("redirect", true);
		this.append   = conf.getAttributeAsBoolean("append", true);

		String out = conf.getAttribute("out", null);
		String err = conf.getAttribute("err", null);
		this.stdOut = toFile(out);
		this.stdErr = toFile(err);
	}

	private File toFile(String path)
	{
		if(path != null)
		{
			return new File(path);
		}
		String tmp = System.getProperty("java.io.tmpdir");
		return new File(tmp + File.separator + stdFile);
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
		return exec(line, baseDir, null, null);
	}

	@Override
	public int exec(List<String> line, File baseDir, File output, File error)
		throws Exception
	{
		ProcessBuilder pb = new ProcessBuilder(line);
		if(baseDir != null)
		{
			pb.directory(baseDir);
		}
		
		pb.inheritIO();
		if(redirect)
		{
			Redirect redirect = toRedirect(output, stdOut);
			pb.redirectOutput(redirect);
			
			redirect = toRedirect(error, stdErr);
			pb.redirectError(redirect);
		}

		Process p = pb.start();
		return p.waitFor();
	}

	private Redirect toRedirect(File file, File whenNull)
	{
		File target = file == null ? whenNull : file;
		Redirect redirect = append ? Redirect.appendTo(target) : Redirect.to(target);
		return redirect;
	}
}
