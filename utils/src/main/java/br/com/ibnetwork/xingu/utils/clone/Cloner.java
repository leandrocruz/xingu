package br.com.ibnetwork.xingu.utils.clone;

public interface Cloner
{
	<T> T deepClone(T t)
		throws CloneException;

	void addHandler(Class<?> clazz, ReferenceHandler handler);
}
