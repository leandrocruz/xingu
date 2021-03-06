package xingu.utils.classloader;

public interface ClassLoaderFactory
{
	NamedClassLoader buildClassLoader(String name, NamedClassLoader parent)
		throws Exception;
}
