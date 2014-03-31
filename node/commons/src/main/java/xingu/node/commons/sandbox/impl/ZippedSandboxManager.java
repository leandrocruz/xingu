package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URL;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.sandbox.Sandbox;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.impl.Pulga;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.DirectoryClassLoader;
import br.com.ibnetwork.xingu.utils.io.zip.ZipUtils;

/*
 * Loads a Sandbox from a zip file
 */
public class ZippedSandboxManager
	extends SandboxManagerImpl
	implements Configurable, Initializable
{
	@Inject
	private Factory						factory;

	private File						local;
	
	private String						remote;

	private Logger						logger				= LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		conf               = conf.getChild("repo");
		String      remote = conf.getAttribute("remote");
		String      local  = conf.getAttribute("local");
		this.local         = new File(local);
		this.remote        = remote;
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
		
		File[] files = local.listFiles((FileFilter) new SuffixFileFilter(".zip"));
		for(File file : files)
		{
			logger.info("Loading Sandbox from '{}'", file);
			try
			{
				Sandbox sandbox = from(file);
				logger.info("Sandbox '{}' loaded", sandbox);
				register(sandbox);
			}
			catch(Exception e)
			{
				logger.warn("Error loading Sandbox from: " + file, e);
			}
		}
	}

	private Sandbox from(File file)
		throws Exception
	{
		String      id        = idFrom(file);
		File        src       = sourceDirectoryFor(id);
		ClassLoader cl        = buildClassLoader(id, src);
		Container   container = buildContainer(cl);
		
		return new SandboxImpl(id, container, cl);
	}

	private Container buildContainer(ClassLoader cl)
		throws Exception
	{
		Sandbox  system = byId(Sandbox.SYSTEM);
		Container parent = system.container();
		URL       url    = cl.getResource("pulga-bot.xml");
		logger.info("Pulga file is '{}'", url);

		InputStream is    = url.openStream();
		Container   pulga = new Pulga(/*parent,*/ is, cl);
		pulga.configure();
		pulga.start();
		return pulga;
	}

	private ClassLoader buildClassLoader(String id, File src)
		throws Exception
	{
		Sandbox    system = byId(Sandbox.SYSTEM);
		ClassLoader parent = system.classLoader();
		return new DirectoryClassLoader(src).buildClassLoader(id, parent);
	}

	private String idFrom(File file)
	{
		String name = file.getName();
		int    idx  = name.lastIndexOf(".");
		return name.substring(0, idx);
	}
	
	private File sourceDirectoryFor(String id)
		throws Exception
	{
		File src = new File(local, id);
		if(!src.exists())
		{
			File zip = new File(local, id + ".zip");
			if(!zip.exists())
			{
				zip = getRemote(id);
			}
			ZipUtils.explode(zip, src);
		}
		return src;
	}

	private File getRemote(String id)
		throws Exception
	{
		File   file = new File(local, id + ".zip");
		String url  = remote + "/" + file.getName();
		logger.info("Bundle '{}' not found on local repo. Downloading from '{}'", id, url);
		
//		HttpResponse res  = http.get(url).exec();
//		InputStream  is   = res.getRawBody();
//		byte[]       data = IOUtils.toByteArray(is);
//		FileUtils.writeByteArrayToFile(file, data);
//		return file;

		throw new NotImplementedYet("Can't load remote packages");
	}

}
