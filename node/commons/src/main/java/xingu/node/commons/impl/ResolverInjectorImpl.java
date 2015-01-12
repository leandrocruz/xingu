package xingu.node.commons.impl;

import xingu.container.Inject;
import xingu.container.Injector;
import xingu.node.commons.ResolverInjector;

public class ResolverInjectorImpl
	implements ResolverInjector
{
	@Inject
	private Injector injector;
	
	@Override
	public void injectDependencies(Object obj)
		throws Exception
	{
		injector.injectDependencies(obj);
	}

}
