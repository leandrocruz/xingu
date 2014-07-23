package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxDescriptor;
import xingu.node.commons.sandbox.SandboxManager;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.container.impl.Pulga;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderUtils;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.impl.ClassLoaderAdapter;
import br.com.ibnetwork.xingu.utils.classloader.impl.DirectoryClassLoader;
import br.com.ibnetwork.xingu.utils.io.zip.ZipUtils;

public abstract class SandboxManagerSupport
	implements SandboxManager, Configurable, Initializable
{
	protected String				containerFile;

	private Map<String, Sandbox>	sandboxById	= new HashMap<String, Sandbox>();

	protected Logger				logger		= LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		containerFile = conf.getChild("container").getAttribute("file", "pulga-sandbox.xml");
	}
	
	@Override
	public void initialize()
		throws Exception
	{
		NamedClassLoader cl = new ClassLoaderAdapter(Sandbox.SYSTEM, Thread.currentThread().getContextClassLoader());
		Container container = ContainerUtils.getLocalContainer();
		register(new SandboxImpl(Sandbox.SYSTEM, container, cl));
		
		SandboxDescriptor[] descriptors = getSandboxDescriptors();
		if(descriptors != null)
		{
			for(SandboxDescriptor desc : descriptors)
			{
				register(desc);
			}
		}
	}

	private void register(SandboxDescriptor desc)
	{
		logger.info("Loading Sandbox from '{}'", desc);
		try
		{
			Sandbox sandbox = toSandbox(desc);
			register(sandbox);
			logger.info("Sandbox '{}' loaded", sandbox);
		}
		catch(Throwable t)
		{
			logger.warn("Error loading Sandbox from: " + desc, t);
		}
	}

	protected Sandbox toSandbox(SandboxDescriptor desc)
		throws Exception
	{
		String           id        = desc.getId();
		File             src       = sourceDirectoryFor(desc);
		Sandbox          parent    = byId(Sandbox.SYSTEM);
		NamedClassLoader cl        = buildClassLoader(id, src, parent);
		Container        container = buildContainer(cl, parent);

		return new SandboxImpl(id, container, cl);
	}
	
	protected File sourceDirectoryFor(SandboxDescriptor desc)
		throws Exception
	{
		File   file    = desc.getFile();
		String name    = file.getName();
		String dirName = FilenameUtils.getBaseName(name);
		File   dir     = new File(file.getParentFile(), dirName);
		if(!dir.exists())
		{
			ZipUtils.explode(file, dir);
		}
		return dir;
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
		if(url == null)
		{
			throw new NotImplementedYet("Can't find '"+containerFile+"' using this classloader");
		}
		InputStream is     = url.openStream();
		Container   pulga  = new Pulga(parent, is, cl);
		pulga.configure();
		pulga.start();
		return pulga;
	}

	@Override
	public Collection<Sandbox> getAll()
	{
		Collection<Sandbox> values = sandboxById.values();
		return Collections.unmodifiableCollection(values);
	}

	@Override
	public Sandbox sandboxFor(Object obj)
	{
		String id = ClassLoaderUtils.nameFor(obj.getClass());
		if(id == null)
		{
			id = Sandbox.SYSTEM;
		}
		return byId(id);
	}

	@Override
	public Sandbox byId(String id)
	{
		Sandbox result = sandboxById.get(id);
		if(result == null)
		{
			try
			{
				SandboxDescriptor desc = load(id);
				result = toSandbox(desc);
				sandboxById.put(id, result);
			}
			catch(Exception e)
			{
				logger.error("Error loading sandbox '"+id+"'", e);
			}
		}
		return result;
	}

	protected void register(Sandbox sandbox)
	{
		String  id  = sandbox.id();
		Sandbox old = sandboxById.get(id);
		if(old != null)
		{
			throw new NotImplementedYet("Sandbox '"+id+"' can't be replaced");
		}
		sandboxById.put(id, sandbox);
	}

	protected SandboxDescriptor load(String id)
		throws Exception
	{
		throw new NotImplementedYet("Can't load sandbox '"+id+"' on the fly");
	}

	@Override
	public <T> T lookup(String id, Class<T> clazz)
	{
		Sandbox sandbox = byId(id);
		if(sandbox == null)
		{
			throw new NotImplementedYet("Can't load sandbox '"+id+"'");
		}
		Container container = sandbox.container();
		return container.lookup(clazz);
	}

	protected abstract SandboxDescriptor[] getSandboxDescriptors()
		throws Exception;
}