package xingu.node.commons.impl;

import xingu.node.commons.ResolverInjector;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Injector;

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
