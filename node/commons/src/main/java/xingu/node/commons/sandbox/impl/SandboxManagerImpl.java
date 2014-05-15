package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URL;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;
import xingu.node.commons.sandbox.Sandbox;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.impl.Pulga;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.impl.DirectoryClassLoader;
import br.com.ibnetwork.xingu.utils.io.zip.ZipUtils;

public class SandboxManagerImpl
	extends SandboxManagerSupport
	implements Configurable, Initializable
{
	@Inject("sandbox-manager")
	protected HttpClient	http;

	protected File			local;

	protected String		remote;

	protected String		containerFile;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		containerFile = conf.getChild("container").getAttribute("file", "pulga.xml");
		conf          = conf.getChild("repo");
		String remote = conf.getAttribute("remote");
		String local  = conf.getAttribute("local");
		this.local  = new File(local);
		this.remote = remote;
	}

	@Override
	public void initialize()
		throws Exception
	{
		super.initialize();
		if(!local.exists())
		{
			logger.warn("Local Sandbox repository '{}' is empty", local);
			return;
		}

		File[] files = getSandboxFiles();
		for(File file : files)
		{
			logger.info("Loading Sandbox from '{}'", file);
			try
			{
				Sandbox sandbox = from(file);
				register(sandbox);
				logger.info("Sandbox '{}' loaded", sandbox);
			}
			catch(Throwable t)
			{
				logger.warn("Error loading Sandbox from: " + file, t);
			}
		}
	}

	protected File[] getSandboxFiles()
	{
		return local.listFiles((FileFilter) new SuffixFileFilter(".zip"));
	}

	protected File sourceDirectoryFor(String id)
		throws Exception
	{
		File src = new File(local, id);
		if(!src.exists())
		{
			File zip = new File(local, id + ".zip");
			if(!zip.exists())
			{
				throw new NotImplementedYet();
			}
			ZipUtils.explode(zip, src);
		}
		return src;
	}

	private Sandbox from(File file)
		throws Exception
	{
		String           id        = bundleIdFrom(file);
		File             src       = sourceDirectoryFor(id);
		Sandbox          parent    = byId(Sandbox.SYSTEM);
		NamedClassLoader cl        = buildClassLoader(id, src, parent);
		Container        container = buildContainer(cl, parent);

		return new SandboxImpl(id, container, cl);
	}

	private String bundleIdFrom(File file)
	{
		String name = file.getName();
		int    idx  = name.lastIndexOf(".");
		return name.substring(0, idx);
	}

	protected NamedClassLoader buildClassLoader(String id, File src, Sandbox parentSandbox)
		throws Exception
	{
		NamedClassLoader parent = parentSandbox.classLoader();
		return new DirectoryClassLoader(src).buildClassLoader(id, parent);
	}
	
	private Container buildContainer(NamedClassLoader cl, Sandbox parentSandbox)
		throws Exception
	{
		Container   parent = parentSandbox.container();
		URL         url    = cl.getResource(containerFile);
		InputStream is     = url.openStream();
		Container   pulga  = new Pulga(parent, is, cl);
		pulga.configure();
		pulga.start();
		return pulga;
	}

	
	@Override
	protected Sandbox load(String id)
		throws Exception
	{
		File   file = new File(local, id + ".zip");
		String url  = remote + "/" + file.getName();
		logger.info("Bundle '{}' not found on local repo. Downloading from '{}'", id, url);

		HttpResponse res  = http.get(url).exec();
		InputStream  is   = res.getRawBody();
		byte[]       data = IOUtils.toByteArray(is);
		FileUtils.writeByteArrayToFile(file, data);

		Sandbox sandbox = from(file);
		return sandbox;
	}
}