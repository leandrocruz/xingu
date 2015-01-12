package xingu.codec.impl.flexjson;

import java.util.Map;

import xingu.container.Inject;
import xingu.utils.classloader.ClassLoaderManager;
import xingu.utils.classloader.NamedClassLoader;
import flexjson.ClassFinder;

public class XinguClassFinder
	implements ClassFinder
{
	@Inject
	private ClassLoaderManager clm;

	@Override
	public Class loadClass(Map map, String name)
		throws ClassNotFoundException
	{
		String classLoaderName = (String) map.get("classLoader");
		if(classLoaderName == null)
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			return classLoader.loadClass(name);
		}
		
		NamedClassLoader loader = clm.byId(classLoaderName);
		try
		{
			return loader.loadClass(name);
		}
		catch(Exception e)
		{
			throw new ClassNotFoundException("Can't load class '"+name+"'", e);
		}
	}
}
