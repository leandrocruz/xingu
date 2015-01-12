package xingu.container;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.impl.ComponentProxy;
import xingu.lang.NotImplementedYet;
import xingu.utils.FieldUtils;

public class FieldInjector 
    implements Injector
{
    private Container container;
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    public FieldInjector(Container container)
    {
        this.container = container;
    }

    public void injectDependencies(Object obj)
        throws Exception
    {
        List<Field> fields = FieldUtils.getAllFields(obj.getClass());
        for (Field field : fields)
        {
            Inject inject = field.getAnnotation(Inject.class);
            if(inject != null)
            {
                inject(obj, field, inject.value(), inject.lazy());
            }
        }
    }

    private void inject(Object obj, Field field, String key, boolean lazy)
        throws Exception
    {
    	log.debug("Injecting dependencies on {}.{}", obj.getClass().getName(), field.getName());
    	Object component = implForField(obj, field, key, lazy);
        boolean before = field.isAccessible();
        field.setAccessible(true);
        field.set(obj, component);
        field.setAccessible(before);
    }

	private Object implForField(Object obj, Field field, String value, boolean lazy)
		throws Exception
	{
		Class<?> type = field.getType();
    	if(Injector.class.equals(type))
        {
            return this;
        }

    	Container container = containerFor(obj);
		boolean   resolved  = container.isResolved(type, value);
		if(resolved || !lazy)
		{
			try
			{
				return container.lookup(type, value);
			}
			catch(Exception t)
			{
				//log.error("Error injecting dependency '"+type.getName()+"' on '"+obj+"'");
			    throw t;
			}
		}
		
		if(lazy)
    	{
			return ComponentProxy.newProxyFor(container, obj, type, value);
    	}

		throw new NotImplementedYet("Can't inject dependency '"+type.getName()+"' on '"+obj+"'");
	}
	
	protected Container containerFor(Object obj)
	{
		return container;
	}
}
