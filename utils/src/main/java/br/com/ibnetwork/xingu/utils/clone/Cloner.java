package br.com.ibnetwork.xingu.utils.clone;

public interface Cloner
{
	<T> T deepClone(final T t)
		throws CloneException;
}
