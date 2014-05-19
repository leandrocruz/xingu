package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxManager;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderUtils;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.impl.ClassLoaderAdapter;

public abstract class SandboxManagerSupport
	implements SandboxManager, Initializable
{
	private Map<String, Sandbox>	sandboxById	= new HashMap<String, Sandbox>();

	protected Logger				logger		= LoggerFactory.getLogger(getClass());

	@Override
	public void initialize()
		throws Exception
	{
		NamedClassLoader cl = new ClassLoaderAdapter(Sandbox.SYSTEM, Thread.currentThread().getContextClassLoader());
		Container container = ContainerUtils.getLocalContainer();
		register(new SandboxImpl(Sandbox.SYSTEM, container, cl));
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
				result = load(id);
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

	protected abstract Sandbox load(String id)
		throws Exception;

	protected String bundleIdFrom(File file)
	{
		String name = file.getName();
		int    idx  = name.lastIndexOf(".");
		return name.substring(0, idx);
	}
}