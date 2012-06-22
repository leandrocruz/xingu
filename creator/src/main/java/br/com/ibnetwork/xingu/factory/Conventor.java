package br.com.ibnetwork.xingu.factory;

public interface Conventor
{
	Object apply(Class<?> base, String suffix, Object... params)
		throws FactoryException;
}
