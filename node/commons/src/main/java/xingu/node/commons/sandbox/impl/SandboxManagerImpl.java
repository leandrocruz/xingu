package xingu.node.commons.sandbox.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SanbdoxManager;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;

public class SandboxManagerImpl
	implements SanbdoxManager, Initializable
{
	private Map<String, Sandbox> sandboxById = new HashMap<String, Sandbox>();

	@Override
	public void initialize()
		throws Exception
	{
		ClassLoader cl        = Thread.currentThread().getContextClassLoader();
		Container   container = ContainerUtils.getLocalContainer();
		register(new SandboxImpl(Sandbox.SYSTEM, container, cl));
	}

	@Override
	public Sandbox sandboxFor(Object obj)
	{
		ClassLoader cl = obj.getClass().getClassLoader();
		String      id = Sandbox.SYSTEM;
		if(cl instanceof NamedClassLoader)
		{
			id = ((NamedClassLoader) cl).getName();
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
		String   id  = sandbox.id();
		Sandbox old = sandboxById.get(id);
		if(old != null)
		{
			throw new NotImplementedYet("Universes can't be replaced");
		}
		sandboxById.put(id, sandbox);
	}

}
