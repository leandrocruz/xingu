package xingu.node.commons.signal.behavior.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.factory.Conventor;
import xingu.node.commons.ResolverInjector;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.BehaviorResolver;
import xingu.node.commons.signal.behavior.NullBehavior;
import xingu.node.commons.signal.behavior.SignalBehavior;

public class BehaviorResolverImpl
	implements BehaviorResolver
{
	@Inject
	private ResolverInjector				injector;

	@Inject
	private Conventor						conventor;

	@SuppressWarnings("rawtypes")
	private Map<Class<?>, SignalBehavior>	cache	= new HashMap<Class<?>, SignalBehavior>();

	protected Logger						logger	= LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("rawtypes")
	private void inject(SignalBehavior result)
	{
		try
		{
			injector.injectDependencies(result);
		}
		catch(Exception e)
		{
			logger.warn("Error injecting dependencies on " + result, e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T extends Signal<?>> SignalBehavior<T> behaviorFor(Signal<?> signal)
	{
		if(signal instanceof SignalBehavior)
		{
			SignalBehavior behavior = (SignalBehavior) signal;
			inject(behavior);
			return behavior;
		}

		Class<?> clazz = signal.getClass();
		SignalBehavior result = cache.get(clazz);
		if(result != null)
		{
			return result;
		}

		result = (SignalBehavior) conventor.apply(clazz, "Behavior");
		if(result == null)
		{
			result = NullBehavior.instance();
		}
		else
		{
			inject(result);
		}
		cache.put(clazz, result);

		return result;
	}
}
