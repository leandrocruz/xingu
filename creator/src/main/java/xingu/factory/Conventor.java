package xingu.factory;

public interface Conventor
{
	Object apply(Class<?> base, String suffix)
		throws FactoryException;

	Object apply(Class<?> base, String suffix, Object... params)
			throws FactoryException;

}
