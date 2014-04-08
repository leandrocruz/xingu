package xingu.node.commons.sandbox.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxManager;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderUtils;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.impl.ClassLoaderAdapter;

public class SandboxManagerImpl
	implements SandboxManager, Initializable
{
	@Inject
	private ClassLoaderManager		clm;

	private Map<String, Sandbox>	sandboxById	= new HashMap<String, Sandbox>();

	@Override
	public void initialize()
		throws Exception
	{
		NamedClassLoader cl = new ClassLoaderAdapter(Sandbox.SYSTEM, Thread.currentThread().getContextClassLoader());
		Container container = ContainerUtils.getLocalContainer();
		register(new SandboxImpl(Sandbox.SYSTEM, container, cl));
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
		return sandboxById.get(id);
	}

	@Override
	public void register(Sandbox sandbox)
	{
		String  id  = sandbox.id();
		Sandbox old = sandboxById.get(id);
		if(old != null)
		{
			throw new NotImplementedYet("Universes can't be replaced");
		}
		sandboxById.put(id, sandbox);
		clm.register(sandbox.classLoader());
	}
}
