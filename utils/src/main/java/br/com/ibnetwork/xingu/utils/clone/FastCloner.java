package br.com.ibnetwork.xingu.utils.clone;

public interface FastCloner<T>
{
	T clone(T original, Cloner cloner);
}
