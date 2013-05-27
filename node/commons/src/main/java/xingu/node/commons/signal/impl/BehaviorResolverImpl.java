package xingu.node.commons.signal.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.signal.BehaviorResolver;
import xingu.node.commons.signal.NullBehavior;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalBehavior;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Injector;
import br.com.ibnetwork.xingu.factory.Conventor;

public class BehaviorResolverImpl
	implements BehaviorResolver
{
	@Inject
	private Injector						injector;

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
	public <T extends Signal, S extends Signal> SignalBehavior<T, S> behaviorFor(Signal signal)
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
		cache.put(clazz, result);

		return result;
	}
}
